import { useMutation, UseMutationOptions } from 'react-query';
import { AxiosResponse } from 'axios';

import api from 'constants/api';
import HttpError from 'utils/HttpError';

interface Args {
  feedId: number;
}

const postLike = async ({ feedId }: Args) => {
  try {
    const { data } = await api.post(`/feeds/${feedId}/like`);

    return data;
  } catch (error) {
    const { status, data } = error.response;

    console.error(data.message);

    // 잠깐 임시로 작성한 에러 핸들링 코드입니다.
    throw new HttpError(status, data.message);
  }
};

const useFeedLike = (option?: UseMutationOptions<AxiosResponse<unknown>, HttpError, Args>) => {
  return useMutation(postLike, option);
};

export default useFeedLike;
