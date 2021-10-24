import { AuthData } from 'types';

export const MOCK_TOKEN: AuthData = {
  accessToken: {
    value: 'access token value',
    expiredIn: 7200000,
  },
  refreshToken: {
    value: 'refresh token value',
    expiredIn: 1209600000,
  },
};
