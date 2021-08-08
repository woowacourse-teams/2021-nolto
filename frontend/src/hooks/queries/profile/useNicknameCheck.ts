import { useQuery, UseQueryOptions } from 'react-query';

import api from 'constants/api';
import { ErrorHandler, Tech } from 'types';
import HttpError from 'utils/HttpError';
import { resolveHttpErrorResponse } from 'utils/error';

interface CustomQueryOption extends UseQueryOptions<Tech[], HttpError> {
  nickname: string;
  errorHandler?: ErrorHandler;
}

const checkNicknameUsable = async (nickname: string, errorHandler: ErrorHandler) => {
  try {
    const { data } = await api.get(`/members/me/profile/validation?nickname=${nickname}`);

    return data;
  } catch (error) {
    resolveHttpErrorResponse({
      errorResponse: error.response,
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
