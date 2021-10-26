import axios from 'axios';

export const BASE_URL: { [key: string]: string } = {
  development: 'https://nolto-dev.kro.kr',
  production: 'https://nolto.kro.kr',
};

export const backendApi = axios.create({
  baseURL: BASE_URL[process.env.BASE_URL] || BASE_URL.development,
  withCredentials: true,
});

export const frontendApi = axios.create();
