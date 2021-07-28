import { useMutation } from 'react-query';
import { AxiosResponse } from 'axios';

import api from 'constants/api';
import ERROR_CODE from 'constants/errorCode';
import HttpError from 'utils/HttpError';

interface Args {
  feedId: number;
  formData: FormData;
}

const uploadFeed = async ({ feedId, formData }: Args) => {
  try {
    const { data } = await api.put(`/feeds/${feedId}`, formData);

    return data;
  } catch (error) {
    const { status, data } = error.response;

    console.error(data.errorMessage);

    throw new HttpError(status, ERROR_CODE[data.errorCode] || '피드 수정에 에러가 발생했습니다');
  }
};

const useFeedModify = () => {
  return useMutation<AxiosResponse<unknown>, HttpError, Args>(uploadFeed);
};

export default useFeedModify;
