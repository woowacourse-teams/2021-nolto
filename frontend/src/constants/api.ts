import axios from 'axios';

const api = axios.create({
  baseURL: 'https://nolto.kro.kr',
  headers: {
    Authorization:
      'bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMUBlbWFpbC5jb20iLCJpYXQiOjE2MjYzNTM5MzMsImV4cCI6MTYyNjM1NzUzM30.FV6rawx-3CY3ETvIp1tmBsbS1vcKq4pcuzk4aBC8Kcg',
  },
});

export default api;
