import { useQuery, UseQueryOptions } from 'react-query';

import api from 'constants/api';
import HttpError from 'utils/HttpError';
import { Feed, ErrorHandler } from 'types';

interface CustomQueryOption extends UseQueryOptions<Feed[], HttpError> {
  errorHandler?: ErrorHandler;
}

type ErrorType = 'feeds-001' | 'feeds-002';

const getHotFeeds = async (errorHandler?: ErrorHandler) => {
  try {
    const { data } = await api.get('/feeds/hot');

    return data;
  } catch (error) {
    const { status, data } = error.response;

    console.error(data.message);

    const errorMap: Record<ErrorType, string> = {
      ['feeds-001']: '임시 에러 메시지 1',
      ['feeds-002']: '임시 에러 메시지 2',
    };

    throw new HttpError(
      status,
      errorMap[data.error as ErrorType] || '인기 피드에 에러가 발생했습니다',
      errorHandler,
    );
  }
};

const useHotFeeds = ({ errorHandler, ...option }: CustomQueryOption) => {
  return useQuery<Feed[], HttpError>(['hotFeeds'], () => getHotFeeds(errorHandler), option);
};

export default useHotFeeds;
