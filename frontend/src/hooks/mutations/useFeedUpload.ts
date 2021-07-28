import { useMutation } from 'react-query';

import api from 'constants/api';
import ERROR_CODE from 'constants/errorCode';
import HttpError from 'utils/HttpError';

const uploadFeed = async (formData: FormData) => {
  try {
    const { data } = await api.post('/feeds', formData);

    return data;
  } catch (error) {
    const { status, data } = error.response;

    console.error(data.errorMessage);

    throw new HttpError(
      status,
      ERROR_CODE[data.errorCode] || '피드 업로드 과정에서 에러가 발생했습니다',
    );
  }
};

const useFeedUpload = () => {
  return useMutation(uploadFeed);
};

export default useFeedUpload;
