import { useQuery, UseQueryOptions } from 'react-query';

import api from 'constants/api';
import { ErrorHandler, UserHistory } from 'types';
import HttpError from 'utils/HttpError';
import { resolveHttpError } from 'utils/error';

interface CustomQueryOption extends UseQueryOptions<UserHistory, HttpError> {
  errorHandler?: ErrorHandler;
}

const getHistory = async (errorHandler?: ErrorHandler) => {
  try {
    const { data } = await api.get('/members/me/history');

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
  return useQuery<UserHistory>('userHistory', () => getHistory(errorHandler), option);
};

export default useUserHistory;
