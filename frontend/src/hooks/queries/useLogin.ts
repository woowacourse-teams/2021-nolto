import { useMutation } from 'react-query';

import api from 'constants/api';
import { LoginInfo } from 'types';

const postFeed = (loginData: LoginInfo) => api.post('/login', loginData);

export default function useLogin() {
  return useMutation(postFeed);
}
