import React, { createContext, useEffect, useMemo, useState } from 'react';
import { QueryObserverResult, RefetchOptions, useQuery, useQueryClient } from 'react-query';
import axios from 'axios';

import LoginModal from 'components/LoginModal/LoginModal';
import api from 'constants/api';
import QUERY_KEYS from 'constants/queryKeys';
import { ALERT_MSG } from 'constants/message';
import useDialog from 'contexts/dialog/useDialog';
import useModal from 'contexts/modal/useModal';
import { UserInfo } from 'types';
import CustomError from 'utils/CustomError';
import { resolveHttpError } from 'utils/error';
import HttpError from 'utils/HttpError';

interface ContextValue {
  userData: UserInfo;
  isLoggedIn: boolean;
  login: (accessToken: string) => void;
  logout: () => void;
  refetchMember: (options?: RefetchOptions) => Promise<QueryObserverResult<UserInfo, unknown>>;
}

interface Props {
  children: React.ReactNode;
}

export const Context = createContext<ContextValue | null>(null);

const getMember = async (accessToken: string) => {
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
    });
  }
};

interface Props {
  initialUserData?: UserInfo;
}

const MemberProvider = ({ children, initialUserData }: Props) => {
  const queryClient = useQueryClient();
  const modal = useModal();
  const dialog = useDialog();

  const [accessToken, setAccessToken] = useState(null);
  const [isLoggedIn, setIsLoggedIn] = useState(initialUserData ? true : false);

  const logout = () => {
    // TODO: common 부분 api.ts로 추상화
    queryClient.resetQueries(QUERY_KEYS.MEMBER + accessToken);
    setAccessToken('');
    api.defaults.headers.common['Authorization'] = '';
  };

  const login = (accessToken: string) => {
    setAccessToken(accessToken);
  };

  const { data: userData, refetch: refetchMember } = useQuery<UserInfo>(
    QUERY_KEYS.MEMBER + accessToken,
    () => getMember(accessToken),
    {
      suspense: false,
      useErrorBoundary: false,
      placeholderData: initialUserData,
      onError: (error) => {
        if (error instanceof HttpError) {
          dialog.alert(ALERT_MSG.SESSION_EXPIRED);
          logout();
          modal.openModal(<LoginModal />);
        }
      },
    },
  );

  useEffect(() => {
    setIsLoggedIn(!!userData);

    if (!userData) return;

    const registerSessionRequest: { accessToken: string; userData: UserInfo } = {
      accessToken,
      userData: userData,
    };

    axios.post('/auth/session', registerSessionRequest, {
      withCredentials: true,
    });
  }, [userData]);

  const contextValue: ContextValue = useMemo(
    () => ({
      userData,
      isLoggedIn,
      login,
      logout,
      refetchMember,
    }),
    [userData, isLoggedIn],
  );

  return <Context.Provider value={contextValue}>{children}</Context.Provider>;
};

export default MemberProvider;
