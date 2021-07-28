import React, { useEffect, useState, useMemo } from 'react';
import { useQuery, useQueryClient } from 'react-query';

import api from 'constants/api';
import HttpError from 'utils/HttpError';
import CustomError from 'utils/CustomError';
import useModal from 'context/modal/useModal';
import useNotification from 'context/notification/useNotification';
import LoginModal from 'components/LoginModal/LoginModal';

type ErrorType = 'auth-001' | 'auth-002';

const getMember = async () => {
  const token = localStorage.getItem('accessToken') || '';
  console.log('엑세스토큰', token);

  if (!token) {
    throw new CustomError('로그아웃 상태입니다.');
  }

  try {
    const { data } = await api.get('/members/me');

    return data;
  } catch (error) {
    const { status, data } = error.response;

    const errorMap: Record<ErrorType, string> = {
      ['auth-001']: '임시 에러 메시지 1',
      ['auth-002']: '임시 에러 메시지 2',
    };

    throw new HttpError(
      status,
      errorMap[data.error as ErrorType] || '사용자 정보를 불러오는 과정에서 에러가 발생했습니다',
    );
  }
};

const useMember = () => {
  const queryClient = useQueryClient();
  const modal = useModal();
  const notification = useNotification();

  const logout = () => {
    localStorage.removeItem('accessToken');
    queryClient.resetQueries('member');
  };

  const { data: loginData } = useQuery('member', getMember, {
    suspense: false,
    useErrorBoundary: false,
    onError: (error) => {
      if (error instanceof HttpError) {
        notification.alert('로그인 정보가 만료되었습니다. 다시 로그인 해주세요.');
        logout();
        modal.openModal(<LoginModal />);
      }
    },
  });

  const [isLogin, setIsLogin] = useState(false);

  useEffect(() => {
    setIsLogin(loginData ? true : false);
  }, [loginData]);

  return {
    loginData,
    isLogin,
    logout,
  };
};

export default useMember;
