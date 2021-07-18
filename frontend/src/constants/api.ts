import axios from 'axios';

const api = axios.create({
  baseURL: 'https://nolto.kro.kr',
  headers: {
    Authorization:
      'bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMUBlbWFpbC5jb20iLCJpYXQiOjE2MjY1ODUwOTUsImV4cCI6MTYyNjU4ODY5NX0.BXDH0gqE5AqeZGcbDgIQl4z9_qlgPyAX7vzFgGBZRLI',
  },
});

export default api;
