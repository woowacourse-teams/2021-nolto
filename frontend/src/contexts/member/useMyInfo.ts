import { useEffect } from 'react';
import { useQuery, useQueryClient, UseQueryOptions } from 'react-query';

import { backendApi } from 'constants/api';
import QUERY_KEYS from 'constants/queryKeys';
import CustomError from 'utils/CustomError';
import { resolveHttpError } from 'utils/error';
import HttpError from 'utils/HttpError';
import { ErrorHandler, UserInfo } from 'types';

interface CustomQueryOption extends UseQueryOptions<UserInfo, HttpError> {
  accessTokenValue: string;
  errorHandler?: ErrorHandler;
}

export const getMember = async ({ accessTokenValue, errorHandler }: CustomQueryOption) => {
  if (!accessTokenValue) {
    throw new CustomError('로그아웃 상태입니다.');
  }

  backendApi.defaults.headers.common['Authorization'] = `Bearer ${accessTokenValue}`;

  try {
    const { data } = await backendApi.get('/members/me');

    return data;
  } catch (error) {
    resolveHttpError({
      error,
      defaultErrorMessage: '사용자 정보를 불러오는 과정에서 에러가 발생했습니다',
      errorHandler,
    });
  }
};

const useMyInfo = ({ accessTokenValue, errorHandler, ...option }: CustomQueryOption) => {
  const queryClient = useQueryClient();

  const { data, refetch } = useQuery<UserInfo>(
    QUERY_KEYS.MEMBER,
    () => getMember({ accessTokenValue, errorHandler }),
    option,
  );

  useEffect(() => {
    if (!accessTokenValue) return;

    queryClient.cancelQueries(QUERY_KEYS.MEMBER);
    refetch();
  }, [accessTokenValue]);

  return { data, refetch };
};

export default useMyInfo;
