import axios from 'axios';

const api = axios.create({
  baseURL: 'https://nolto.kro.kr',
  headers: {
    Authorization:
      'bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMUBlbWFpbC5jb20iLCJpYXQiOjE2MjY1MDY5MzcsImV4cCI6MTYyNjUxMDUzN30.Ysz8YpzxUOQZYBNMTG5yJREuVuxdg0Atlqx1x_MCUa4',
  },
});

export default api;
