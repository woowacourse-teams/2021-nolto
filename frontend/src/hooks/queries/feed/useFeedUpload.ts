import { useMutation } from 'react-query';

import api from 'constants/api';
import { resolveHttpError } from 'utils/error';

const uploadFeed = async (formData: FormData) => {
  try {
    const { data } = await api.post('/feeds', formData);

    return data;
  } catch (error) {
    resolveHttpError({
      error,
      defaultErrorMessage: '피드 업로드 과정에서 에러가 발생했습니다',
    });
  }
};

const useFeedUpload = () => {
  return useMutation(uploadFeed);
};

export default useFeedUpload;
