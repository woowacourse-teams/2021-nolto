import { useEffect } from 'react';
import { useHistory, useLocation } from 'react-router-dom';

import useUserInfo from 'hooks/useUserInfo';
import api from 'constants/api';
import ROUTE from 'constants/routes';

const useOAuthLogin = (type: 'google' | 'github') => {
  const history = useHistory();
  const location = useLocation();
  const userInfo = useUserInfo();

  const getAccessToken = async (code: string) => {
    const { data } = await api.get<{ accessToken: string }>(
      `/login/oauth/${type}/token?code=${code}`,
    );

    localStorage.setItem('accessToken', data.accessToken);
    userInfo.setUserInfo({
      email: 'dummy1@email.com',
    });
    history.push(ROUTE.HOME);
  };

  useEffect(() => {
    const query = new URLSearchParams(location.search);

    getAccessToken(query.get('code'));
  }, []);
};

export default useOAuthLogin;
