import React, { createContext, useEffect, useMemo, useState } from 'react';
import { QueryObserverResult, RefetchOptions, useQueryClient } from 'react-query';
import axios from 'axios';

import LoginModal from 'components/LoginModal/LoginModal';
import api from 'constants/api';
import QUERY_KEYS from 'constants/queryKeys';
import { ALERT_MSG } from 'constants/message';
import hasWindow from 'constants/windowDetector';
import useDialog from 'contexts/dialog/useDialog';
import useModal from 'contexts/modal/useModal';
import { AuthData, UserInfo } from 'types';
import HttpError from 'utils/HttpError';
import useMyInfo from './useMyInfo';

interface ContextValue {
  userInfo: UserInfo;
  login: (authData: AuthData) => void;
  logout: () => void;
  refetchMember: (options?: RefetchOptions) => Promise<QueryObserverResult<UserInfo, unknown>>;
}

export const Context = createContext<ContextValue | null>(null);

interface Props {
  children: React.ReactNode;
}

const MemberProvider = ({ children }: Props) => {
  const queryClient = useQueryClient();
  const modal = useModal();
  const dialog = useDialog();

  const [accessToken, setAccessToken] = useState(hasWindow ? window.__accessToken__ : '');

  const { data: userInfo, refetch: refetchMember } = useMyInfo({
    accessToken,
    errorHandler: (error) => {
      if (error instanceof HttpError) {
        dialog.alert(ALERT_MSG.SESSION_EXPIRED);
        logout();
        modal.openModal(<LoginModal />);
      }
    },
    suspense: false,
    useErrorBoundary: false,
  });

  const logout = () => {
    // TODO: common 부분 api.ts로 추상화
    queryClient.removeQueries(QUERY_KEYS.MEMBER);
    setAccessToken('');
    api.defaults.headers.common['Authorization'] = '';

    axios.post('/auth/logout', null, {
      withCredentials: true,
    });
  };

  const login = async (authData: AuthData) => {
    setAccessToken(authData?.accessToken);

    axios.post('/auth/login', authData, {
      withCredentials: true,
    });
  };

  useEffect(() => {
    if (!accessToken) return;

    queryClient.cancelQueries(QUERY_KEYS.MEMBER);
    refetchMember();
  }, [accessToken]);

  const contextValue: ContextValue = useMemo(
    () => ({
      userInfo,
      login,
      logout,
      refetchMember,
    }),
    [userInfo],
  );

  return <Context.Provider value={contextValue}>{children}</Context.Provider>;
};

export default MemberProvider;
