import { useMutation } from 'react-query';
import { AxiosResponse } from 'axios';

import api from 'constants/api';
import ERROR_CODE from 'constants/errorCode';
import HttpError from 'utils/HttpError';

interface Args {
  feedId: number;
}

const deleteFeed = async ({ feedId }: Args) => {
  try {
    const { data } = await api.delete(`/feeds/${feedId}`);

    return data;
  } catch (error) {
    const { status, data } = error.response;

    console.error(data.message);

    //TODO: error 타입가드 만들기
    throw new HttpError(
      status,
      ERROR_CODE[data.errorCode] || '피드 삭제 과정에서 에러가 발생했습니다',
    );
  }
};

export default function useFeedDelete() {
  return useMutation<AxiosResponse<unknown>, HttpError, Args>(deleteFeed);
}
