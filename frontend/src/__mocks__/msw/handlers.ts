import { rest } from 'msw';

import { BASE_URL } from 'constants/api';
import { mockFeeds } from '__mocks__/fixture/Feeds';
import { UserInfo } from 'types';
import { MOCK_USER } from '__mocks__/fixture/User';

export const handlers = [
  rest.get(`${BASE_URL.development}/login/oauth/:type/token`, (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json({
        accessToken: '액세스토큰',
      }),
    );
  }),
  rest.get(`${BASE_URL.development}/feeds/recent`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(mockFeeds));
  }),
  rest.post(`${BASE_URL.development}/feeds`, (req, res, ctx) => {
    return res(ctx.status(200));
  }),
  rest.get(`${BASE_URL.development}/members/me`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json<UserInfo>(MOCK_USER.MAZZI));
  }),
];
