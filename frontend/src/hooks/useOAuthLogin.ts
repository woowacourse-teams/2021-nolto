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
    const { data } = await backendApi.get<AuthData>(`/login/oauth/${type}/token?code=${code}`);

    login(data);
    history.push(ROUTE.HOME);
  };

  useEffect(() => {
    const query = new URLSearchParams(location.search);

    getAccessToken(query.get('code'));
  }, []);
};

export default useOAuthLogin;
