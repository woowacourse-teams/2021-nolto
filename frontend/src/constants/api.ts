import axios from 'axios';

export const BASE_URL: { [key: string]: string } = {
  development: 'https://nolto-dev.kro.kr',
  production: 'https://nolto.kro.kr',
};

const api = axios.create({
  baseURL: BASE_URL[process.env.NODE_ENV] || BASE_URL.development,
});

api.interceptors.request.use(
  (config) => {
    const accessToken = localStorage.getItem('accessToken');
    if (accessToken) {
      config.headers['Authorization'] = 'Bearer ' + accessToken;
    }
    return config;
  },
  (error) => Promise.reject(error),
);

export default api;
