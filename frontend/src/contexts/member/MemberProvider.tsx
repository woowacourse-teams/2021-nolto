import React, { createContext, useMemo } from 'react';
import { QueryObserverResult, RefetchOptions, useQueryClient } from 'react-query';

import LoginModal from 'components/LoginModal/LoginModal';
import { backendApi, frontendApi } from 'constants/api';
import QUERY_KEYS from 'constants/queryKeys';
import { ALERT_MSG } from 'constants/message';
import useDialog from 'contexts/dialog/useDialog';
import useModal from 'contexts/modal/useModal';
import useAccessToken from 'hooks/useAccessToken';
import { AuthData, UserInfo } from 'types';
import HttpError from 'utils/HttpError';
import useMyInfo from './useMyInfo';

interface ContextValue {
  userInfo: UserInfo | undefined;
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

  const [accessToken, setAccessToken] = useAccessToken();

  const { data: userInfo, refetch: refetchMember } = useMyInfo({
    accessTokenValue: accessToken ? accessToken.value : '',
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

  const login = async (authData: AuthData) => {
    setAccessToken(authData.accessToken);

    frontendApi.post('/auth/login', authData);
  };

  const logout = () => {
    // TODO: common 부분 backendApi.ts로 추상화
    queryClient.removeQueries(QUERY_KEYS.MEMBER);
    setAccessToken(null);
    backendApi.defaults.headers.common['Authorization'] = '';

    frontendApi.post('/auth/logout');
  };

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
