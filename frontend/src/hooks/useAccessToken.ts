import { useEffect, useState } from 'react';

import hasWindow from 'constants/windowDetector';
import { frontendApi } from 'constants/api';
import { AuthData, Token } from 'types';

const TOKEN_RENEW_BUFFER = 5 * 60 * 1000;

const useAccessToken = () => {
  const [accessToken, setAccessToken] = useState<Token | null>(
    hasWindow
      ? { value: window.__accessTokenValue__, expiredIn: window.__accessTokenExpiredIn__ }
      : null,
  );

  useEffect(() => {
    if (!accessToken?.value) {
      return;
    }

    const timerId = setTimeout(async () => {
      const { data: authData } = await frontendApi.post<AuthData>('/auth/renewToken');

      setAccessToken(authData.accessToken);
    }, accessToken.expiredIn - TOKEN_RENEW_BUFFER);

    return () => clearTimeout(timerId);
  }, [accessToken]);

  return [accessToken, setAccessToken] as const;
};

export default useAccessToken;
