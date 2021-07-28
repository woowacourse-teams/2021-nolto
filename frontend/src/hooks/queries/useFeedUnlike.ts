import { useMutation, UseMutationOptions } from 'react-query';
import { AxiosResponse } from 'axios';

import api from 'constants/api';
import HttpError from 'utils/HttpError';

interface Args {
  feedId: number;
}

const postUnlike = async ({ feedId }: Args) => {
  try {
    const { data } = await api.post(`/feeds/${feedId}/unlike `);

    return data;
  } catch (error) {
    const { status, data } = error.response;

    console.error(data.message);
    // 잠깐 임시로 작성한 에러 핸들링 코드입니다.
    throw new HttpError(status, data.error);
  }
};

const useFeedUnlike = (option?: UseMutationOptions<AxiosResponse<unknown>, HttpError, Args>) => {
  return useMutation(postUnlike, option);
};

export default useFeedUnlike;
