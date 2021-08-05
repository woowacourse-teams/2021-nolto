import React, { useEffect, useState } from 'react';
import { useQuery, useQueryClient } from 'react-query';

import api from 'constants/api';
import HttpError from 'utils/HttpError';
import CustomError from 'utils/CustomError';
import useModal from 'context/modal/useModal';
import useDialog from 'context/dialog/useDialog';
import LoginModal from 'components/LoginModal/LoginModal';
import { UserInfo } from 'types';
import { resolveHttpErrorResponse } from 'utils/error';

const getMember = async () => {
  const token = localStorage.getItem('accessToken') || '';

  if (!token) {
    throw new CustomError('로그아웃 상태입니다.');
  }

  try {
    const { data } = await api.get('/members/me');

    return data;
  } catch (error) {
    resolveHttpErrorResponse({
      errorResponse: error.response,
      defaultErrorMessage: '사용자 정보를 불러오는 과정에서 에러가 발생했습니다',
    });
  }
};

const useMember = () => {
  const queryClient = useQueryClient();
  const modal = useModal();
  const dialog = useDialog();

  const logout = () => {
    localStorage.removeItem('accessToken');
    queryClient.resetQueries('member');
  };

  const { data: userData } = useQuery<UserInfo>('member', getMember, {
    suspense: false,
    useErrorBoundary: false,
    onError: (error) => {
      if (error instanceof HttpError) {
        dialog.alert('로그인 정보가 만료되었습니다. 다시 로그인 해주세요.');
        logout();
        modal.openModal(<LoginModal />);
      }
    },
  });

  const [isLogin, setIsLogin] = useState(false);

  useEffect(() => {
    setIsLogin(userData ? true : false);
  }, [userData]);

  return {
    userData,
    isLogin,
    logout,
  };
};

export default useMember;
