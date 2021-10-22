import { useQuery, UseQueryOptions } from 'react-query';

import { backendApi } from 'constants/api';
import QUERY_KEYS from 'constants/queryKeys';
import { ErrorHandler, NotificationType } from 'types';
import HttpError from 'utils/HttpError';
import { resolveHttpError } from 'utils/error';

interface CustomQueryOption extends UseQueryOptions<NotificationType[], HttpError> {
  errorHandler?: ErrorHandler;
}

const getNotifications = async (errorHandler?: ErrorHandler) => {
  try {
    const { data } = await backendApi.get('/members/me/notifications');

    return data;
  } catch (error) {
    resolveHttpError({
      error,
      defaultErrorMessage: '알림 목록을 불러오는 과정에서 에러가 발생했습니다',
      errorHandler,
    });
  }
};

const useNotiLoad = ({ errorHandler, ...option }: CustomQueryOption) => {
  return useQuery<NotificationType[]>(
    QUERY_KEYS.NOTIFICATION,
    () => getNotifications(errorHandler),
    option,
  );
};

export default useNotiLoad;
