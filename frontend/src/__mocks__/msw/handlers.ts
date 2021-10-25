import { BASE_URL } from 'constants/api';
import { rest } from 'msw';

import { CommentType, FeedDetail, Tech, UserInfo, AuthData } from 'types';
import { MOCK_TOKEN } from '__mocks__/fixture/auth';
import { MOCK_COMMENTS, MOCK_SUB_COMMENTS } from '__mocks__/fixture/comments';
import { MOCK_FEED_DETAIL, MOCK_HOT_FEEDS, MOCK_RECENT_FEEDS } from '__mocks__/fixture/feeds';
import { MOCK_HISTORY } from '__mocks__/fixture/history';
import { MOCK_NOTI } from '__mocks__/fixture/noti';
import { MOCK_PROFILE } from '__mocks__/fixture/profile';
import { MOCK_TECHS } from '__mocks__/fixture/tags';
import { MOCK_USER } from '__mocks__/fixture/user';

const LOCAL_URL = 'http://localhost';

export const handlers = [
  rest.get(`${BASE_URL.development}/login/oauth/:type/token`, (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json({
        accessToken: '액세스토큰',
      }),
    );
  }),

  // feeds
  rest.get(`${BASE_URL.development}/feeds/hot`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(MOCK_HOT_FEEDS));
  }),
  rest.get(`${BASE_URL.development}/feeds/recent`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(MOCK_RECENT_FEEDS));
  }),
  rest.get(`${BASE_URL.development}/feeds/:feedId`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(MOCK_FEED_DETAIL));
  }),
  rest.post(`${BASE_URL.development}/feeds`, (req, res, ctx) => {
    return res(ctx.status(201));
  }),
  rest.put(`${BASE_URL.development}/feeds/:feedId`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json<FeedDetail>(MOCK_FEED_DETAIL));
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

  // members
  rest.get(`${BASE_URL.development}/members/me`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json<UserInfo>(MOCK_USER.MAZZI));
  }),
  rest.get(`${BASE_URL.development}/members/me/history`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(MOCK_HISTORY));
  }),
  rest.get(`${BASE_URL.development}/members/me/profile`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(MOCK_PROFILE));
  }),
  rest.get(`${BASE_URL.development}/members/me/profile/validation`, (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json({
        isUsable: true,
      }),
    );
  }),
  rest.get(`${BASE_URL.development}/members/me/notifications`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(MOCK_NOTI));
  }),

  // Comments
  rest.get(`${BASE_URL.development}/feeds/:feedId/comments`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json<CommentType[]>(MOCK_COMMENTS));
  }),
  rest.get(`${BASE_URL.development}/feeds/:feedId/comments/:commentId/replies`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json<CommentType[]>(MOCK_SUB_COMMENTS));
  }),

  // tags
  rest.get(`${BASE_URL.development}/tags/techs`, (req, res, ctx) => {
    const query = req.url.searchParams;
    const auto_complete = query.get('auto_complete');

    if (auto_complete) {
      return res(ctx.status(200), ctx.json<Tech[]>(MOCK_TECHS));
    }

    return res(ctx.status(404));
  }),
  rest.get(`${BASE_URL.development}/tags/techs/search`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json<Tech[]>(MOCK_TECHS));
  }),
  rest.get(`${BASE_URL.development}/tags/techs/trend`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json<Tech[]>(MOCK_TECHS));
  }),

  // accessToken
  rest.post(`${LOCAL_URL}/auth/renewToken`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json<AuthData>(MOCK_TOKEN));
  }),
];
