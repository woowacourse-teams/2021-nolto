import { useInfiniteQuery, UseInfiniteQueryOptions } from 'react-query';

import { backendApi } from 'constants/api';
import QUERY_KEYS from 'constants/queryKeys';
import { ErrorHandler, Feed, FeedStep, SearchParams } from 'types';
import HttpError from 'utils/HttpError';
import { resolveHttpError } from 'utils/error';

interface CustomQueryOption extends UseInfiniteQueryOptions<InfiniteFeedResponse, HttpError> {
  step?: FeedStep;
  help?: boolean;
  searchParams: SearchParams;
  nextFeedId?: number;
  countPerPage?: number;
  errorHandler?: ErrorHandler;
}

interface InfiniteFeedResponse {
  feeds: Feed[];
  nextFeedId: number;
}

const getSearchResult = async ({
  searchParams,
  step,
  help,
  nextFeedId,
  countPerPage,
  errorHandler,
}: CustomQueryOption) => {
  const { query, techs } = searchParams;

  try {
    const { data } = await backendApi.get('/feeds/search', {
      params: { query, techs, step: step || '', help, nextFeedId, countPerPage },
    });

    return data;
  } catch (error) {
    resolveHttpError({
      error,
      defaultErrorMessage: '검색 과정에서 에러가 발생했습니다',
      errorHandler,
    });
  }
};

const useSearch = ({
  step,
  help,
  searchParams,
  countPerPage,
  errorHandler,
  ...options
}: CustomQueryOption) => {
  return useInfiniteQuery<InfiniteFeedResponse, HttpError>(
    [QUERY_KEYS.SEARCH_RESULT, { step, help, countPerPage, searchParams }],
    ({ pageParam }) =>
      getSearchResult({
        step,
        help,
        searchParams,
        nextFeedId: pageParam,
        countPerPage,
        errorHandler,
      }),
    {
      getNextPageParam: (lastPage) => lastPage.nextFeedId ?? false,
      ...options,
    },
  );
};

export default useSearch;
