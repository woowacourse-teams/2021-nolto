import { useMutation } from 'react-query';
import { AxiosResponse } from 'axios';

import api from 'constants/api';
import HttpError from 'utils/HttpError';
import { resolveHttpErrorResponse } from 'utils/error';

interface Args {
  feedId: number;
}

const deleteFeed = async ({ feedId }: Args) => {
  try {
    const { data } = await api.delete(`/feeds/${feedId}`);

    return data;
  } catch (error) {
    resolveHttpErrorResponse({
      errorResponse: error.response,
      defaultErrorMessage: '피드 삭제 과정에서 에러가 발생했습니다',
    });
  }
};

export default function useFeedDelete() {
  return useMutation<AxiosResponse<unknown>, HttpError, Args>(deleteFeed);
}
