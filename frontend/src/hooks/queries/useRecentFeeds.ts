import { useQuery, UseQueryOptions } from 'react-query';

import api from 'constants/api';
import HttpError from 'utils/HttpError';
import { Feed, FilterType, ErrorHandler } from 'types';
import { resolveHttpErrorResponse } from 'utils/error';

interface CustomQueryOption extends UseQueryOptions<Feed[], HttpError> {
  filter?: FilterType;
  errorHandler?: ErrorHandler;
}

const getRecentFeeds = async (filter: FilterType, errorHandler: ErrorHandler) => {
  try {
    const { data } = await api.get('/feeds/recent', { params: { filter } });

    return data;
  } catch (error) {
    resolveHttpErrorResponse({
      errorResponse: error.response,
      defaultErrorMessage: '최신 피드에 에러가 발생했습니다',
      errorHandler,
    });
  }
};

const useRecentFeeds = ({ filter, errorHandler, ...option }: CustomQueryOption) => {
  return useQuery<Feed[]>(
    ['recentFeeds', filter],
    () => getRecentFeeds(filter, errorHandler),
    option,
  );
};

export default useRecentFeeds;
