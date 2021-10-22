import { useInfiniteQuery, UseInfiniteQueryOptions } from 'react-query';

import { backendApi } from 'constants/api';
import QUERY_KEYS from 'constants/queryKeys';
import HttpError from 'utils/HttpError';
import { resolveHttpError } from 'utils/error';
import { Feed, FeedStep, ErrorHandler } from 'types';

interface CustomQueryOption extends UseInfiniteQueryOptions<InfiniteFeedResponse, HttpError> {
  step?: FeedStep;
  help?: boolean;
  nextFeedId?: number;
  countPerPage?: number;
  errorHandler?: ErrorHandler;
}

interface InfiniteFeedResponse {
  feeds: Feed[];
  nextFeedId: number;
}

export const loadRecentFeeds = async ({
  step,
  help,
  nextFeedId,
  countPerPage,
  errorHandler,
}: CustomQueryOption) => {
  try {
    const { data } = await backendApi.get('/feeds/recent', {
      params: { step, help, nextFeedId, countPerPage },
    });

    return data;
  } catch (error) {
    resolveHttpError({
      error,
      defaultErrorMessage: '최신 피드에 에러가 발생했습니다',
      errorHandler,
    });
  }
};

const useRecentFeedsLoad = ({
  step,
  help,
  countPerPage,
  errorHandler,
  ...options
}: CustomQueryOption) => {
  return useInfiniteQuery<InfiniteFeedResponse, HttpError>(
    [QUERY_KEYS.RECENT_FEEDS, { step, help, countPerPage }],
    ({ pageParam }) =>
      loadRecentFeeds({ step, help, nextFeedId: pageParam, countPerPage, errorHandler }),
    {
      getNextPageParam: (lastPage) => lastPage.nextFeedId ?? false,
      ...options,
    },
  );
};

export default useRecentFeedsLoad;
