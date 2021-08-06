import { useQuery, UseQueryOptions } from 'react-query';

import api from 'constants/api';
import HttpError from 'utils/HttpError';
import { Feed, FilterType, ErrorHandler } from 'types';
import { resolveHttpError } from 'utils/error';

interface CustomQueryOption extends UseQueryOptions<Feed[], HttpError> {
  filter?: FilterType;
  errorHandler?: ErrorHandler;
}

const loadRecentFeeds = async (filter: FilterType, errorHandler: ErrorHandler) => {
  try {
    const { data } = await api.get('/feeds/recent', { params: { filter } });

    return data;
  } catch (error) {
    resolveHttpError({
      error,
      defaultErrorMessage: '최신 피드에 에러가 발생했습니다',
      errorHandler,
    });
  }
};

const useRecentFeedsLoad = ({ filter, errorHandler, ...option }: CustomQueryOption) => {
  return useQuery<Feed[]>(
    ['recentFeeds', filter],
    () => loadRecentFeeds(filter, errorHandler),
    option,
  );
};

export default useRecentFeedsLoad;
