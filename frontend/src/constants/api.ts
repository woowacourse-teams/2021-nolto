import axios from 'axios';

import hasWindow from 'constants/windowDetector';

export const BASE_URL: { [key: string]: string } = {
  development: 'https://nolto-dev.kro.kr',
  production: 'https://nolto.kro.kr',
};

const api = axios.create({
  baseURL: BASE_URL[process.env.BASE_URL] || BASE_URL.development,
});

api.interceptors.request.use(
  (config) => {
    config.withCredentials = true;

    const accessToken = hasWindow && localStorage.getItem('accessToken');
    if (accessToken) {
      config.headers['Authorization'] = 'Bearer ' + accessToken;
    }
    return config;
  },
  (error) => Promise.reject(error),
);

export default api;
