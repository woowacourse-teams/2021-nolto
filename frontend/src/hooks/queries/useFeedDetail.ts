import { useQuery, UseQueryOptions } from 'react-query';

import api from 'constants/api';
import { ErrorHandler, FeedDetail } from 'types';
import HttpError from 'utils/HttpError';
import { resolveHttpErrorResponse } from 'utils/error';

interface CustomQueryOption extends UseQueryOptions<FeedDetail, HttpError> {
  errorHandler?: ErrorHandler;
  id: number;
}

const getFeedDetail = async (id: number, errorHandler: ErrorHandler) => {
  try {
    const { data } = await api.get(`/feeds/${id}`);

    return data;
  } catch (error) {
    resolveHttpErrorResponse({
      errorResponse: error.response,
      defaultErrorMessage: '피드 상세 정보에 에러가 발생했습니다',
      errorHandler,
    });
  }
};

const useFeedDetail = ({ errorHandler, id, ...option }: CustomQueryOption) => {
  return useQuery<FeedDetail>(['feedDetail', id], () => getFeedDetail(id, errorHandler), option);
};

export default useFeedDetail;
