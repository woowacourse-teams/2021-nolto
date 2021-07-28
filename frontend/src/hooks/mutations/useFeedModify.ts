import { useMutation } from 'react-query';
import { AxiosResponse } from 'axios';

import api from 'constants/api';
import HttpError from 'utils/HttpError';
import { resolveHttpErrorResponse } from 'utils/error';

interface Args {
  feedId: number;
  formData: FormData;
}

const uploadFeed = async ({ feedId, formData }: Args) => {
  try {
    const { data } = await api.put(`/feeds/${feedId}`, formData);

    return data;
  } catch (error) {
    resolveHttpErrorResponse({
      errorResponse: error.response,
      defaultErrorMessage: '피드 수정에 에러가 발생했습니다',
    });
  }
};

const useFeedModify = () => {
  return useMutation<AxiosResponse<unknown>, HttpError, Args>(uploadFeed);
};

export default useFeedModify;
