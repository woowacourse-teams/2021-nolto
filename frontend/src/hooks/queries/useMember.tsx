import React, { useEffect, useState } from 'react';
import { useQuery, useQueryClient } from 'react-query';

import api from 'constants/api';
import QUERY_KEYS from 'constants/queryKeys';
import { ALERT_MSG } from 'constants/message';
import hasWindow from 'constants/windowDetector';
import HttpError from 'utils/HttpError';
import useModal from 'contexts/modal/useModal';
import useDialog from 'contexts/dialog/useDialog';
import LoginModal from 'components/LoginModal/LoginModal';
import { UserInfo } from 'types';
import { resolveHttpError } from 'utils/error';

const getMember = async (): Promise<UserInfo> => {
  const token = (hasWindow && localStorage.getItem('accessToken')) || '';

  if (!token) return;

  try {
    const { data } = await api.get('/members/me');

    return data;
  } catch (error) {
    resolveHttpError({
      error,
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

  const { data: userData, refetch: refetchMember } = useQuery<UserInfo>(
    QUERY_KEYS.MEMBER,
    getMember,
    {
      suspense: false,
      useErrorBoundary: false,
      onError: (error) => {
        if (error instanceof HttpError) {
          dialog.alert(ALERT_MSG.SESSION_EXPIRED);
          logout();
          modal.openModal(<LoginModal />);
        }
      },
    },
  );

  const [isLogin, setIsLogin] = useState(false);

  useEffect(() => {
    setIsLogin(userData ? true : false);
  }, [userData]);

  return {
    userData,
    isLogin,
    logout,
    refetchMember,
  };
};

export default useMember;
