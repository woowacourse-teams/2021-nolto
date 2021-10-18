import { useEffect } from 'react';
import { useQuery, useQueryClient, UseQueryOptions } from 'react-query';

import api from 'constants/api';
import QUERY_KEYS from 'constants/queryKeys';
import CustomError from 'utils/CustomError';
import { resolveHttpError } from 'utils/error';
import HttpError from 'utils/HttpError';
import { ErrorHandler, UserInfo } from 'types';

interface CustomQueryOption extends UseQueryOptions<UserInfo, HttpError> {
  accessToken: string;
  errorHandler?: ErrorHandler;
}

export const getMember = async ({ accessToken, errorHandler }: CustomQueryOption) => {
  if (!accessToken) {
    throw new CustomError('로그아웃 상태입니다.');
  }

  api.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;

  try {
    const { data } = await api.get('/members/me');

    return data;
  } catch (error) {
    resolveHttpError({
      error,
      defaultErrorMessage: '사용자 정보를 불러오는 과정에서 에러가 발생했습니다',
      errorHandler,
    });
  }
};

const useMyInfo = ({ accessToken, errorHandler, ...option }: CustomQueryOption) => {
  const queryClient = useQueryClient();

  const { data, refetch } = useQuery<UserInfo>(
    QUERY_KEYS.MEMBER,
    () => getMember({ accessToken, errorHandler }),
    option,
  );

  useEffect(() => {
    if (!accessToken) return;

    queryClient.cancelQueries(QUERY_KEYS.MEMBER);
    refetch();
  }, [accessToken]);

  return { data, refetch };
};

export default useMyInfo;
