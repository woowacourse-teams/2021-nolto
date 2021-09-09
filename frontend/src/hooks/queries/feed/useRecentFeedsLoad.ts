import { useInfiniteQuery, UseInfiniteQueryOptions } from 'react-query';

import api from 'constants/api';
import QUERY_KEYS from 'constants/queryKeys';
import HttpError from 'utils/HttpError';
import { resolveHttpError } from 'utils/error';
import { Feed, FilterType, ErrorHandler } from 'types';

interface CustomQueryOption extends UseInfiniteQueryOptions<InfiniteFeedResponse, HttpError> {
  filter?: FilterType;
  nextFeedId?: number;
  countPerPage?: number;
  errorHandler?: ErrorHandler;
}
interface InfiniteFeedResponse {
  feeds: Feed[];
  nextFeedId: number;
}

const loadRecentFeeds = async ({
  filter,
  nextFeedId,
  countPerPage,
  errorHandler,
}: CustomQueryOption) => {
  try {
    const { data } = await api.get('/feeds/recent', {
      params: { filter, nextFeedId, countPerPage },
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
  filter,
  countPerPage,
  errorHandler,
  ...options
}: CustomQueryOption) => {
  return useInfiniteQuery<InfiniteFeedResponse, HttpError>(
    [QUERY_KEYS.RECENT_FEEDS, { filter, countPerPage }],
    ({ pageParam }) =>
      loadRecentFeeds({ filter, nextFeedId: pageParam, countPerPage, errorHandler }),
    {
      getNextPageParam: (lastPage) => lastPage.nextFeedId ?? false,
      ...options,
    },
  );
};

export default useRecentFeedsLoad;
