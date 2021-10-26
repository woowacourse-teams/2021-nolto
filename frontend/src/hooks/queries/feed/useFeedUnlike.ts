import { useMutation, UseMutationOptions } from 'react-query';
import { AxiosResponse } from 'axios';

import { backendApi } from 'constants/api';
import HttpError from 'utils/HttpError';
import { resolveHttpError } from 'utils/error';

interface Args {
  feedId: number;
}

const postUnlike = async ({ feedId }: Args) => {
  try {
    const { data } = await backendApi.post(`/feeds/${feedId}/unlike `);

    return data;
  } catch (error) {
    resolveHttpError({
      error,
      defaultErrorMessage: '피드 좋아요 취소 과정에서 에러가 발생했습니다',
    });
  }
};

const useFeedUnlike = (option?: UseMutationOptions<AxiosResponse<unknown>, HttpError, Args>) => {
  return useMutation(postUnlike, option);
};

export default useFeedUnlike;
