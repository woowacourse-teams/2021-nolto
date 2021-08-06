import { useMutation } from 'react-query';
import { AxiosResponse } from 'axios';

import api from 'constants/api';
import HttpError from 'utils/HttpError';
import { resolveHttpError } from 'utils/error';

interface Args {
  feedId: number;
}

const deleteFeed = async ({ feedId }: Args) => {
  try {
    const { data } = await api.delete(`/feeds/${feedId}`);

    return data;
  } catch (error) {
    resolveHttpError({
      error,
      defaultErrorMessage: '피드 삭제 과정에서 에러가 발생했습니다',
    });
  }
};

const useFeedDelete = () => {
  return useMutation<AxiosResponse<unknown>, HttpError, Args>(deleteFeed);
};

export default useFeedDelete;
