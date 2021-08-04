import { rest } from 'msw';

import { BASE_URL } from 'constants/api';
import { mockFeeds } from '__mocks__/fixture/Feeds';
import { UserInfo } from 'types';

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
    return res(
      ctx.status(200),
      ctx.json<UserInfo>({
        id: 1,
        imageUrl:
          'https://avatars.githubusercontent.com/u/48755175?s=400&u=1dbaae3d7765dba9692d9b8eb35c5a6bc7c2b5b1&v=4',
        nickname: '지그',
        socialType: 'GITHUB',
      }),
    );
  }),
];
