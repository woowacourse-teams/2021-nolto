import { useEffect } from 'react';
import { useHistory, useLocation } from 'react-router-dom';

import useMember from 'contexts/member/useMember';
import { backendApi } from 'constants/api';
import ROUTE from 'constants/routes';
import { AuthData } from 'types';

const useOAuthLogin = (type: 'google' | 'github') => {
  const history = useHistory();
  const location = useLocation();
  const { login } = useMember();

  const getAccessToken = async (code: string) => {
    const { data: authData } = await backendApi.get<AuthData>(
      `/login/oauth/${type}/token?code=${code}`,
    );

    login(authData);
    history.push(ROUTE.HOME);
  };

  useEffect(() => {
    const query = new URLSearchParams(location.search);

    const code = query.get('code');

    if (!code) {
      alert('잘못된 접근입니다.');
      return;
    }

    getAccessToken(code);
  }, []);
};

export default useOAuthLogin;
