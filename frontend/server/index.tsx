import express from 'express';
import path from 'path';
import cookieParser from 'cookie-parser';

import QUERY_KEYS from 'constants/queryKeys';
import { RECENT_FEEDS_PER_PAGE } from 'constants/common';
import { getFeedDetail } from 'hooks/queries/feed/useFeedDetail';
import { loadHotFeeds } from 'hooks/queries/feed/useHotFeedsLoad';
import { loadRecentFeeds } from 'hooks/queries/feed/useRecentFeedsLoad';
import { isFeedStep } from 'utils/typeGuard';
import authRoute from './auth';
import { generateResponse } from './utils';

const PORT = Number(process.env.PORT) || 9000;
const app = express();

app.use(express.json());

app.use(express.static(path.resolve(__dirname, '../dist'), { index: false }));

app.use(cookieParser());

app.use('/auth', authRoute);

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

app.listen(PORT, '0.0.0.0', () => {
  console.log(`Server is listening on port ${PORT}`);
});
