import { useMutation } from 'react-query';
import { AxiosResponse } from 'axios';

import api from 'constants/api';
import HttpError from 'utils/HttpError';
import { resolveHttpError } from 'utils/error';

interface Args {
  notificationId?: number;
}

const deleteNoti = async ({ notificationId }: Args) => {
  try {
    const endpoint = notificationId
      ? `/members/me/notifications/${notificationId}`
      : '/members/me/notifications';

    const { data } = await api.delete(endpoint);

    return data;
  } catch (error) {
    resolveHttpError({
      error,
      defaultErrorMessage: '알림 삭제 과정에서 에러가 발생했습니다',
    });
  }
};

export default function useNotiDelete() {
  return useMutation<AxiosResponse<unknown>, HttpError, Args>(deleteNoti);
}
