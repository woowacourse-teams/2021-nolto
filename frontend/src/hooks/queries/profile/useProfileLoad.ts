import { useQuery, UseQueryOptions } from 'react-query';

import api from 'constants/api';
import { ErrorHandler, Profile } from 'types';
import HttpError from 'utils/HttpError';
import { resolveHttpError } from 'utils/error';

interface CustomQueryOption extends UseQueryOptions<Profile, HttpError> {
  errorHandler?: ErrorHandler;
}

const getProfile = async (errorHandler?: ErrorHandler) => {
  try {
    const { data } = await api.get('/members/me/profile');

    return data;
  } catch (error) {
    resolveHttpError({
      error,
      defaultErrorMessage: '사용자 프로필을 불러오는 과정에서 에러가 발생했습니다',
      errorHandler,
    });
  }
};

const useProfileLoad = ({ errorHandler, ...option }: CustomQueryOption) => {
  return useQuery<Profile>('profile', () => getProfile(errorHandler), option);
};

export default useProfileLoad;
