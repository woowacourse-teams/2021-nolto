import { QueryFunctionContext, QueryKey, useQuery, UseQueryOptions } from 'react-query';

import api from 'constants/api';
import HttpError from 'utils/HttpError';
import { Feed, FilterType, ErrorHandler } from 'types';

interface CustomQueryOption extends UseQueryOptions<Feed[], HttpError> {
  errorHandler?: ErrorHandler;
  filter?: FilterType;
}

type ErrorType = 'feeds-001' | 'feeds-002';

const getRecentFeeds =
  (errorHandler: ErrorHandler) =>
  async ({ queryKey }: QueryFunctionContext<QueryKey, FilterType>) => {
    const [_, filter] = queryKey;
    try {
      const { data } = await api.get('/feeds/recent', { params: { filter } });
      return data;
    } catch (error) {
      const { status, data } = error.response;

      const errorMap: Record<ErrorType, string> = {
        ['feeds-001']: '임시 에러 메시지 1',
        ['feeds-002']: '임시 에러 메시지 2',
      };

      throw new HttpError(
        status,
        errorMap[data.error as ErrorType] || '최신 피드에 에러가 발생했습니다',
        errorHandler,
      );
    }
  };

export default function useRecentFeeds({ errorHandler, filter, ...option }: CustomQueryOption) {
  return useQuery<Feed[]>(['recentFeeds', filter], getRecentFeeds(errorHandler), option);
}
