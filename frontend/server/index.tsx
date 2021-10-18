import React from 'react';
import ReactDOMServer from 'react-dom/server';
import { dehydrate, Hydrate, QueryClient, QueryClientProvider, useQuery } from 'react-query';
import { StaticRouter } from 'react-router';
import { ServerStyleSheet, StyleSheetManager } from 'styled-components';
import { ChunkExtractor } from '@loadable/server';
import serialize from 'serialize-javascript';
import { FilledContext, HelmetProvider } from 'react-helmet-async';
import axios from 'axios';

import express from 'express';
import path from 'path';
import fs from 'fs';
import cookieParser from 'cookie-parser';

import App from '../src/App';
import QUERY_KEYS from 'constants/queryKeys';
import { RECENT_FEEDS_PER_PAGE } from 'constants/common';
import api from 'constants/api';
import { getMember } from 'contexts/member/useMyInfo';
import { getFeedDetail } from 'hooks/queries/feed/useFeedDetail';
import { loadHotFeeds } from 'hooks/queries/feed/useHotFeedsLoad';
import { loadRecentFeeds } from 'hooks/queries/feed/useRecentFeedsLoad';
import { isFeedStep } from 'utils/typeGuard';
import { AuthData } from 'types';

const PORT = Number(process.env.PORT) || 9000;
const PUBLIC_IP_API = 'https://api.ipify.org/?format=text';
const app = express();
const sheet = new ServerStyleSheet();

app.use(express.json());

app.use(express.static(path.resolve(__dirname, '../dist'), { index: false }));

app.use(cookieParser());

const statsFile = path.resolve(__dirname, '../dist/loadable-stats.json');

const extractor = new ChunkExtractor({ statsFile });

type PrefetchCallback = (queryClient: QueryClient) => Promise<void>;

const getNewAuthToken = async (req: express.Request): Promise<AuthData> => {
  if (!req.cookies.refreshToken) return;

  const { refreshToken } = req.cookies;

  let clientIP = req.ip;

  if (process.env.NODE_ENV !== 'production') {
    const { data: publicIP } = await axios.get(PUBLIC_IP_API);
    clientIP = publicIP;
  }

  try {
    const { data: authData } = await api.post<AuthData>('/login/oauth/refreshToken', {
      refreshToken,
      clientIP,
    });

    return authData;
  } catch (error) {
    console.error(error.response);
  }
};

const generateResponse = async (
  req: express.Request,
  res: express.Response,
  prefetchCallback?: PrefetchCallback,
) => {
  const newAuthData = await getNewAuthToken(req);

  const queryClient = new QueryClient({
    defaultOptions: {
      queries: {
        suspense: true,
        useErrorBoundary: true,
        retry: 1,
        staleTime: Infinity,
      },
    },
  });

  if (newAuthData) {
    res.cookie('refreshToken', newAuthData.refreshToken, {
      httpOnly: true,
      maxAge: newAuthData.expiredIn,
    });

    await queryClient.prefetchQuery(QUERY_KEYS.MEMBER, () =>
      getMember({ accessToken: newAuthData.accessToken }),
    );
  }

  if (typeof prefetchCallback === 'function') {
    await prefetchCallback(queryClient);
  }

  const dehydratedState = dehydrate(queryClient);

  const helmetContext: Partial<FilledContext> = {};

  const jsx = extractor.collectChunks(
    <QueryClientProvider client={queryClient}>
      <HelmetProvider context={helmetContext}>
        <Hydrate state={dehydratedState}>
          <StyleSheetManager sheet={sheet.instance}>
            <StaticRouter location={req.url}>
              <App />
            </StaticRouter>
          </StyleSheetManager>
        </Hydrate>
      </HelmetProvider>
    </QueryClientProvider>,
  );

  const reactApp = ReactDOMServer.renderToString(jsx);

  const { helmet } = helmetContext;

  const styleTags = sheet.getStyleTags();

  const scriptTags = extractor.getScriptTags();

  const reactQueryState = `<script>window.__REACT_QUERY_STATE__ = ${serialize(dehydratedState, {
    isJSON: true,
  })}</script>`;

  const accessTokenScript = newAuthData
    ? `<script>window.__accessToken__ = "${newAuthData.accessToken}"</script>`
    : '';

  const indexFile = path.resolve(__dirname, '../dist/index.html');

  fs.readFile(indexFile, 'utf8', (err, data) => {
    if (err) {
      console.error('Node.js 서버에서 에러가 발생했습니다.', err);
      return res.status(500).send('서버에서 에러가 발생했습니다. 🔫');
    }

    const result = data
      .replace('<div id="root"></div>', `<div id="root">${reactApp}</div>`)
      .replace(
        /<head>(.+)<\/head>/s,
        `<head>$1 
          ${styleTags} ${scriptTags} ${reactQueryState} ${accessTokenScript}
          ${helmet.title.toString()} 
          ${helmet.link.toString()} 
        </head>`,
      );

    return res.send(result);
  });
};

app.get('/', (req, res) => {
  generateResponse(req, res, async (queryClient) => {
    await queryClient.prefetchQuery(QUERY_KEYS.HOT_FEEDS, () => loadHotFeeds());
  });
});

app.get('/recent', (req, res) => {
  const step = String(req.query.step) || null;
  const help = Boolean(req.query.help);

  generateResponse(req, res, async (queryClient) => {
    await queryClient.prefetchInfiniteQuery(
      [QUERY_KEYS.RECENT_FEEDS, { step, help, countPerPage: RECENT_FEEDS_PER_PAGE }],
      ({ pageParam }) =>
        loadRecentFeeds({
          step: isFeedStep(step) ? step : null,
          help,
          nextFeedId: pageParam,
          countPerPage: RECENT_FEEDS_PER_PAGE,
        }),
    );
  });
});

app.get('/feeds/:feedId', (req, res) => {
  generateResponse(req, res, async (queryClient) => {
    await queryClient.prefetchQuery([QUERY_KEYS.FEED_DETAIL, Number(req.params.feedId)], () =>
      getFeedDetail(Number(req.params.feedId)),
    );
  });
});

app.get('/*', (req, res) => {
  generateResponse(req, res);
});

app.post('/auth/login', (req, res) => {
  const { body } = req;
  const isAuthRequest = body?.accessToken && body?.refreshToken && body?.expiredIn;

  if (!isAuthRequest) {
    res.status(400).send('올바른 요청 양식이 아닙니다.');

    return;
  }

  res
    .cookie('refreshToken', body.refreshToken, {
      httpOnly: true,
      maxAge: body.expiredIn,
    })
    .status(200)
    .send('true');
});

app.post('/auth/logout', (_, res) => {
  res.clearCookie('refreshToken').status(200).send('true');
});

app.post('/auth/renewToken', async (req, res) => {
  const { accessToken, refreshToken, expiredIn } = await getNewAuthToken(req);

  res
    .cookie('refreshToken', refreshToken, {
      httpOnly: true,
      maxAge: expiredIn,
    })
    .status(200)
    .json({ accessToken });
});

app.listen(PORT, '0.0.0.0', () => {
  console.log(`Server is listening on port ${PORT}`);
});
