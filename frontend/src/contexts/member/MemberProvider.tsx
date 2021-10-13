import React, { createContext, useMemo, useState } from 'react';
import { QueryObserverResult, RefetchOptions, useQueryClient } from 'react-query';
import axios from 'axios';

import LoginModal from 'components/LoginModal/LoginModal';
import api from 'constants/api';
import QUERY_KEYS from 'constants/queryKeys';
import { ALERT_MSG } from 'constants/message';
import useDialog from 'contexts/dialog/useDialog';
import useModal from 'contexts/modal/useModal';
import { AuthData, UserInfo } from 'types';
import HttpError from 'utils/HttpError';
import useMyInfo from './useMyInfo';

interface ContextValue {
  userInfo: UserInfo;
  isLoggedIn: boolean;
  login: (authData: AuthData) => void;
  logout: () => void;
  refetchMember: (options?: RefetchOptions) => Promise<QueryObserverResult<UserInfo, unknown>>;
}

interface Props {
  children: React.ReactNode;
}

export const Context = createContext<ContextValue | null>(null);

interface Props {
  initialUserInfo?: UserInfo;
}

const MemberProvider = ({ children, initialUserInfo }: Props) => {
  const queryClient = useQueryClient();
  const modal = useModal();
  const dialog = useDialog();

  const [accessToken, setAccessToken] = useState(null);
  const [isLoggedIn, setIsLoggedIn] = useState(initialUserInfo ? true : false);

  const logout = () => {
    // TODO: common 부분 api.ts로 추상화
    queryClient.resetQueries(QUERY_KEYS.MEMBER);
    setAccessToken('');
    setIsLoggedIn(false);
    api.defaults.headers.common['Authorization'] = '';
  };

  const login = (authData: AuthData) => {
    setAccessToken(authData?.accessToken);
    setIsLoggedIn(true);

    axios.post('/auth', authData, {
      withCredentials: true,
    });
  };

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
    placeholderData: initialUserInfo,
  });

  const contextValue: ContextValue = useMemo(
    () => ({
      userInfo,
      isLoggedIn,
      login,
      logout,
      refetchMember,
    }),
    [userInfo, isLoggedIn],
  );

  return <Context.Provider value={contextValue}>{children}</Context.Provider>;
};

export default MemberProvider;
