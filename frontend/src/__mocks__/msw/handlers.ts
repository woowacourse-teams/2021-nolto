import { rest } from 'msw';

import { BASE_URL } from 'constants/api';
import { MOCK_FEEDS, MOCK_FEED_DETAIL } from '__mocks__/fixture/Feeds';
import { CommentType, FeedDetail, UserInfo } from 'types';
import { MOCK_USER } from '__mocks__/fixture/User';
import { MOCK_COMMENTS, MOCK_SUB_COMMENTS } from '__mocks__/fixture/Comments';

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
  rest.get(`${BASE_URL.development}/members/me`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json<UserInfo>(MOCK_USER.MAZZI));
  }),

  //Comments
  rest.get(`${BASE_URL.development}/feeds/:feedId/comments`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json<CommentType[]>(MOCK_COMMENTS));
  }),
  rest.get(`${BASE_URL.development}/feeds/:feedId/comments/:commentId/replies`, (req, res, ctx) => {
    return res(ctx.status(200), ctx.json<CommentType[]>(MOCK_SUB_COMMENTS));
  }),
];
