import { rest } from 'msw';

import { BASE_URL } from 'constants/api';
import { MOCK_FEEDS, MOCK_FEED_DETAIL } from '__mocks__/fixture/Feeds';
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
    return res(ctx.status(200), ctx.json(MOCK_FEEDS));
  }),
  rest.get(`${BASE_URL.development}/feeds/:feedId`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(MOCK_FEED_DETAIL));
  }),
  rest.post(`${BASE_URL.development}/feeds`, (req, res, ctx) => {
    return res(ctx.status(201));
  }),
  rest.put(`${BASE_URL.development}/feeds/:feedId`, (req, res, ctx) => {
    return res(ctx.status(200));
  }),
  rest.delete(`${BASE_URL.development}/feeds/:feedId`, (req, res, ctx) => {
    return res(ctx.status(204));
  }),
  rest.post(`${BASE_URL.development}/feeds/:feedId/like`, (req, res, ctx) => {
    return res(ctx.status(200));
  }),
  rest.post(`${BASE_URL.development}/feeds/:feedId/unlike`, (req, res, ctx) => {
    return res(ctx.status(200));
  }),
  rest.get(`${BASE_URL.development}/members/me`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json<UserInfo>(MOCK_USER.MAZZI));
  }),
];
