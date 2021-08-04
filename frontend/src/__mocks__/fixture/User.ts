import { UserInfo } from 'types';

type UserName = 'MICKEY' | 'ZIG' | 'MAZZI' | 'JOEL' | 'CHARLIE' | 'POMO';

export const MOCK_USER: Record<UserName, UserInfo> = {
  MICKEY: {
    id: 1,
    socialType: 'GITHUB',
    nickname: '미키',
    imageUrl: 'https://avatars.githubusercontent.com/u/48755175?v=4',
  },
  ZIG: {
    id: 2,
    socialType: 'GOOGLE',
    nickname: '지그',
    imageUrl: 'https://avatars.githubusercontent.com/u/44080404?v=4',
  },
  MAZZI: {
    id: 3,
    socialType: 'GITHUB',
    nickname: '마찌',
    imageUrl: 'https://avatars.githubusercontent.com/u/43840561?v=4',
  },
  JOEL: {
    id: 4,
    socialType: 'GOOGLE',
    nickname: '조엘',
    imageUrl: 'https://avatars.githubusercontent.com/u/61370901?v=4',
  },
  CHARLIE: {
    id: 5,
    socialType: 'GITHUB',
    nickname: '찰리',
    imageUrl: 'https://avatars.githubusercontent.com/u/57378410?v=4',
  },
  POMO: {
    id: 6,
    socialType: 'GOOGLE',
    nickname: '포모',
    imageUrl: 'https://avatars.githubusercontent.com/u/34594339?v=4',
  },
};
