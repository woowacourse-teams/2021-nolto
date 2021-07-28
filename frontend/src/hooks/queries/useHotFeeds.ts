import { useQuery, UseQueryOptions } from 'react-query';

import api from 'constants/api';
import ERROR_CODE from 'constants/errorCode';
import HttpError from 'utils/HttpError';
import { Feed, ErrorHandler } from 'types';

interface CustomQueryOption extends UseQueryOptions<Feed[], HttpError> {
  errorHandler?: ErrorHandler;
}

const getHotFeeds = async (errorHandler?: ErrorHandler) => {
  try {
    const { data } = await api.get('/feeds/hot');

    return data;
  } catch (error) {
    const { status, data } = error.response;

    console.error(data.errorMessage);

    throw new HttpError(
      status,
      ERROR_CODE[data.errorCode] || '인기 피드에 에러가 발생했습니다',
      errorHandler,
    );
  }
};

const useHotFeeds = ({ errorHandler, ...option }: CustomQueryOption) => {
  return useQuery<Feed[], HttpError>(['hotFeeds'], () => getHotFeeds(errorHandler), option);
};

export default useHotFeeds;
