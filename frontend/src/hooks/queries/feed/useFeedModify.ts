import { useMutation } from 'react-query';
import { AxiosResponse } from 'axios';

import { backendApi } from 'constants/api';
import HttpError from 'utils/HttpError';
import { resolveHttpError } from 'utils/error';

interface Args {
  feedId: number;
  formData: FormData;
}

const uploadFeed = async ({ feedId, formData }: Args) => {
  try {
    const { data } = await backendApi.put(`/feeds/${feedId}`, formData);

    return data;
  } catch (error) {
    resolveHttpError({
      error,
      defaultErrorMessage: '피드 수정에 에러가 발생했습니다',
    });
  }
};

const useFeedModify = () => {
  return useMutation<AxiosResponse<unknown>, HttpError, Args>(uploadFeed);
};

export default useFeedModify;
