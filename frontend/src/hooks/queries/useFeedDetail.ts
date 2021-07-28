import { QueryFunctionContext, QueryKey, useQuery, UseQueryOptions } from 'react-query';

import api from 'constants/api';
import { ErrorHandler, FeedDetail } from 'types';
import HttpError from 'utils/HttpError';

interface CustomQueryOption extends UseQueryOptions<FeedDetail, HttpError> {
  errorHandler?: ErrorHandler;
  id: number;
}

type ErrorType = 'feeds-001' | 'feeds-002';

const getFeedDetail = async (id: number, errorHandler: ErrorHandler) => {
  try {
    const { data } = await api.get(`/feeds/${id}`);

    return data;
  } catch (error) {
    const { status, data } = error.response;

    const errorMap: Record<ErrorType, string> = {
      ['feeds-001']: '임시 에러 메시지 1',
      ['feeds-002']: '임시 에러 메시지 2',
    };

    throw new HttpError(
      status,
      errorMap[data.error as ErrorType] || '피드 상세 정보에 에러가 발생했습니다',
      errorHandler,
    );
  }
};

export default function useFeedDetail({ errorHandler, id, ...option }: CustomQueryOption) {
  return useQuery<FeedDetail>(['feedDetail', id], () => getFeedDetail(id, errorHandler), option);
}
