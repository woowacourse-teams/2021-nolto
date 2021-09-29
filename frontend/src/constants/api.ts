import axios from 'axios';

export const BASE_URL: { [key: string]: string } = {
  development: 'https://nolto-dev.kro.kr',
  production: 'https://nolto.kro.kr',
};

const api = axios.create({
  baseURL: BASE_URL[process.env.BASE_URL] || BASE_URL.development,
});

export default api;
