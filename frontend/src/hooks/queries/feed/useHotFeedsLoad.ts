import { useQuery, UseQueryOptions } from 'react-query';

import api from 'constants/api';
import QUERY_KEYS from 'constants/queryKeys';
import HttpError from 'utils/HttpError';
import { Feed, ErrorHandler } from 'types';
import { resolveHttpError } from 'utils/error';

interface CustomQueryOption extends UseQueryOptions<Feed[], HttpError> {
  errorHandler?: ErrorHandler;
}

export const loadHotFeeds = async (errorHandler?: ErrorHandler) => {
  try {
    const { data } = await api.get('/feeds/hot');

    return data;
  } catch (error) {
    resolveHttpError({
      error,
      defaultErrorMessage: '인기 피드에 에러가 발생했습니다',
      errorHandler,
    });
  }
};

const useHotFeedsLoad = ({ errorHandler, ...option }: CustomQueryOption) => {
  return useQuery<Feed[], HttpError>(
    QUERY_KEYS.HOT_FEEDS,
    () => loadHotFeeds(errorHandler),
    option,
  );
};

export default useHotFeedsLoad;
