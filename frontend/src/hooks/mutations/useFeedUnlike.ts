import { useMutation, UseMutationOptions } from 'react-query';
import { AxiosResponse } from 'axios';

import api from 'constants/api';
import HttpError from 'utils/HttpError';
import { resolveHttpErrorResponse } from 'utils/error';

interface Args {
  feedId: number;
}

const postUnlike = async ({ feedId }: Args) => {
  try {
    const { data } = await api.post(`/feeds/${feedId}/unlike `);

    return data;
  } catch (error) {
    resolveHttpErrorResponse({
      errorResponse: error.response,
      defaultErrorMessage: '피드 좋아요 취소 과정에서 에러가 발생했습니다',
    });
  }
};

const useFeedUnlike = (option?: UseMutationOptions<AxiosResponse<unknown>, HttpError, Args>) => {
  return useMutation(postUnlike, option);
};

export default useFeedUnlike;
