import { useQuery, UseQueryOptions } from 'react-query';

import api from 'constants/api';
import { ErrorHandler } from 'types';
import HttpError from 'utils/HttpError';
import { resolveHttpError } from 'utils/error';

interface CustomQueryOption extends UseQueryOptions<{ isUsable: boolean }, HttpError> {
  nickname: string;
  errorHandler?: ErrorHandler;
}

const checkNicknameUsable = async (nickname: string, errorHandler: ErrorHandler) => {
  try {
    const { data } = await api.get(`/members/me/profile/validation?nickname=${nickname}`);

    return data;
  } catch (error) {
    resolveHttpError({
      error,
      defaultErrorMessage: '닉네임 중복검사 과정에서 에러가 발생했습니다',
      errorHandler,
    });
  }
};

const useNicknameCheck = ({ nickname, errorHandler }: CustomQueryOption) => {
  return useQuery<{ isUsable: boolean }>(
    ['nicknameCheck', nickname],
    () => checkNicknameUsable(nickname, errorHandler),
    {
      enabled: !!nickname,
      suspense: false,
    },
  );
};

export default useNicknameCheck;
