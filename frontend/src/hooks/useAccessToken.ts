import { useEffect, useState } from 'react';
import axios from 'axios';

import hasWindow from 'constants/windowDetector';
import api from 'constants/api';

const EXPIRED_IN = 71400000;

const useAccessToken = () => {
  const [accessToken, setAccessToken] = useState(hasWindow ? window.__accessToken__ : '');

  useEffect(() => {
    const timerId = setTimeout(async () => {
      const {
        data: { accessToken },
      } = await axios.post<{ accessToken: string }>('/auth/renewToken');

      setAccessToken(accessToken);

      api.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;
    }, EXPIRED_IN);

    return () => clearTimeout(timerId);
  }, [accessToken]);

  return [accessToken, setAccessToken] as const;
};

export default useAccessToken;
