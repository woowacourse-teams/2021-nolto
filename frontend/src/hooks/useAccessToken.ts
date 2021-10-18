import { useEffect, useState } from 'react';

import hasWindow from 'constants/windowDetector';
import { backendApi, frontendApi } from 'constants/api';

const EXPIRED_IN = 71400000;

const useAccessToken = () => {
  const [accessToken, setAccessToken] = useState(hasWindow ? window.__accessToken__ : '');

  useEffect(() => {
    const timerId = setTimeout(async () => {
      const {
        data: { accessToken },
      } = await frontendApi.post<{ accessToken: string }>('/auth/renewToken');

      setAccessToken(accessToken);

      backendApi.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;
    }, EXPIRED_IN);

    return () => clearTimeout(timerId);
  }, [accessToken]);

  return [accessToken, setAccessToken] as const;
};

export default useAccessToken;
