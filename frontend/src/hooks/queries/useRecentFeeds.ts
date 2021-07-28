import { useQuery, UseQueryOptions } from 'react-query';

import api from 'constants/api';
import ERROR_CODE from 'constants/errorCode';
import HttpError from 'utils/HttpError';
import { Feed, FilterType, ErrorHandler } from 'types';

interface CustomQueryOption extends UseQueryOptions<Feed[], HttpError> {
  filter?: FilterType;
  errorHandler?: ErrorHandler;
}

const getRecentFeeds = async (filter: FilterType, errorHandler: ErrorHandler) => {
  try {
    const { data } = await api.get('/feeds/recent', { params: { filter } });

    return data;
  } catch (error) {
    const { status, data } = error.response;

    console.error(data.errorMessage);

    throw new HttpError(
      status,
      ERROR_CODE[data.errorCode] || '최신 피드에 에러가 발생했습니다',
      errorHandler,
    );
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
