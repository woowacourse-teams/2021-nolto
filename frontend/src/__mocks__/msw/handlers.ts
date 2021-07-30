import { rest } from 'msw';

import { BASE_URL } from 'constants/api';
import { mockFeeds } from '__mocks__/fixture/Feeds';

export const handlers = [
  rest.get(`${BASE_URL.production}/login/oauth/:type/token`, (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json({
        accessToken: '액세스토큰',
      }),
    );
  }),
  rest.get(`${BASE_URL.production}/feeds/recent`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(mockFeeds));
  }),
  rest.post(`${BASE_URL.production}/feeds`, (req, res, ctx) => {
    return res(ctx.status(200));
  }),
];
