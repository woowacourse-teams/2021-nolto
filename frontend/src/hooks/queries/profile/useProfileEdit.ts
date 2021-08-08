import { useMutation } from 'react-query';
import { AxiosResponse } from 'axios';

import api from 'constants/api';
import HttpError from 'utils/HttpError';
import { resolveHttpErrorResponse } from 'utils/error';

interface Args {
  formData: FormData;
}

const editProfile = async ({ formData }: Args) => {
  try {
    const { data } = await api.put('/members/me/profile', formData);

    return data;
  } catch (error) {
    resolveHttpErrorResponse({
      errorResponse: error.response,
      defaultErrorMessage: '프로필 수정 과정에서 에러가 발생했습니다',
    });
  }
};

const useProfileEdit = () => {
  return useMutation<AxiosResponse<unknown>, HttpError, Args>(editProfile);
};

export default useProfileEdit;
