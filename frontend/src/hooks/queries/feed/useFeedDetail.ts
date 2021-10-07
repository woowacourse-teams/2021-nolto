import { useQuery, UseQueryOptions } from 'react-query';

import api from 'constants/api';
import QUERY_KEYS from 'constants/queryKeys';
import { ErrorHandler, FeedDetail } from 'types';
import HttpError from 'utils/HttpError';
import { resolveHttpError } from 'utils/error';

interface CustomQueryOption extends UseQueryOptions<FeedDetail, HttpError> {
  errorHandler?: ErrorHandler;
  feedId: number;
}

export const getFeedDetail = async (feedId: number, errorHandler?: ErrorHandler) => {
  try {
    const { data } = await api.get(`/feeds/${feedId}`);

    return data;
  } catch (error) {
    resolveHttpError({
      error,
      defaultErrorMessage: '피드 상세 정보에 에러가 발생했습니다',
      errorHandler,
    });
  }
};

const useFeedDetail = ({ errorHandler, feedId, ...option }: CustomQueryOption) => {
  return useQuery<FeedDetail, HttpError>(
    [QUERY_KEYS.FEED_DETAIL, feedId],
    () => getFeedDetail(feedId, errorHandler),
    option,
  );
};

export default useFeedDetail;
