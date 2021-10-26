import { useQuery, UseQueryOptions } from 'react-query';

import { backendApi } from 'constants/api';
import QUERY_KEYS from 'constants/queryKeys';
import { ErrorHandler, UserHistory } from 'types';
import HttpError from 'utils/HttpError';
import { resolveHttpError } from 'utils/error';

interface CustomQueryOption extends UseQueryOptions<UserHistory, HttpError> {
  errorHandler?: ErrorHandler;
}

const getHistory = async (errorHandler?: ErrorHandler) => {
  try {
    const { data } = await backendApi.get('/members/me/history');

    return data;
  } catch (error) {
    resolveHttpError({
      error,
      defaultErrorMessage: '사용자 히스토리를 불러오는 과정에서 에러가 발생했습니다',
      errorHandler,
    });
  }
};

const useUserHistory = ({ errorHandler, ...option }: CustomQueryOption) => {
  return useQuery<UserHistory>(QUERY_KEYS.USER_HISTORY, () => getHistory(errorHandler), option);
};

export default useUserHistory;
