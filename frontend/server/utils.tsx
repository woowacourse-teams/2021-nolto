import React from 'react';
import ReactDOMServer from 'react-dom/server';
import { dehydrate, Hydrate, QueryClient, QueryClientProvider } from 'react-query';
import { StaticRouter } from 'react-router';
import { ServerStyleSheet, StyleSheetManager } from 'styled-components';
import serialize from 'serialize-javascript';
import { FilledContext, HelmetProvider } from 'react-helmet-async';
import { ChunkExtractor } from '@loadable/server';
import axios from 'axios';

import express from 'express';
import path from 'path';
import fs from 'fs';

import App from '../src/App';
import QUERY_KEYS from 'constants/queryKeys';
import { backendApi } from 'constants/api';
import { getMember } from 'contexts/member/useMyInfo';
import { AuthData } from 'types';

const statsFile = path.resolve(__dirname, '../dist/loadable-stats.json');
const PUBLIC_IP_API = 'https://api.ipify.org/?format=text';

export const getNewAuthToken = async (req: express.Request): Promise<AuthData> => {
  if (!req.cookies.refreshToken) return;

  const { refreshToken } = req.cookies;

  let clientIP = req.headers['x-forwarded-for'];

  if (process.env.NODE_ENV !== 'production') {
    const { data: publicIP } = await axios.get(PUBLIC_IP_API);
    clientIP = publicIP;
  }

  try {
    const { data: authData } = await backendApi.post<AuthData>('/login/oauth/refreshToken', {
      refreshToken,
      clientIP,
    });

    return authData;
  } catch (error) {
    console.error(error.response);
  }
};

type PrefetchCallback = (queryClient: QueryClient) => Promise<void>;

export const generateResponse = async (
  req: express.Request,
  res: express.Response,
  prefetchCallback?: PrefetchCallback,
) => {
  const sheet = new ServerStyleSheet();
  const extractor = new ChunkExtractor({ statsFile });
  const newAuthData = await getNewAuthToken(req);

  const queryClient = new QueryClient({
    defaultOptions: {
      queries: {
        suspense: true,
        useErrorBoundary: true,
        retry: 1,
      },
    },
  });

  if (newAuthData) {
    res.cookie('refreshToken', newAuthData.refreshToken.value, {
      httpOnly: true,
      maxAge: newAuthData.refreshToken.expiredIn,
    });

    await queryClient.prefetchQuery(QUERY_KEYS.MEMBER, () =>
      getMember({ accessTokenValue: newAuthData.accessToken.value }),
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

  const chunkScriptTags = extractor.getScriptTags();

  const reactQueryState = `<script>window.__REACT_QUERY_STATE__ = ${serialize(dehydratedState, {
    isJSON: true,
  })}</script>`;

  const accessTokenScript = newAuthData
    ? `
    <script>
      window.__accessTokenValue__ = "${newAuthData.accessToken.value}";
      window.__accessTokenExpiredIn__ = ${newAuthData.accessToken.expiredIn};
    </script>
    `
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
          ${styleTags} ${chunkScriptTags} ${reactQueryState} ${accessTokenScript}
          ${helmet.title.toString()} 
          ${helmet.link.toString()} 
        </head>`,
      );

    return res.send(result);
  });
};
