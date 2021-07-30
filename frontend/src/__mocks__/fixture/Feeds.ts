import { Feed, FeedStatus } from 'types';

export const mockFeeds: Feed[] = [
  {
    author: {
      id: 48,
      nickname: '신지혜',
      imageUrl:
        'https://lh3.googleusercontent.com/a-/AOh14GhbQ0AVOOZCZgjWPFAEqOAsvpxf8M4G7-WRycFf3w=s96-c',
    },
    id: 22,
    title: '개쩌는 마찌의 지하철 미션',
    content: '기깔나죠?',
    step: FeedStatus.PROGRESS,
    sos: true,
    thumbnailUrl: 'https://dksykemwl00pf.cloudfront.net/1627525584455ZG9nZG9nLmdpZg==.gif',
  },
  {
    author: {
      id: 44,
      nickname: 'SungSiHyung',
      imageUrl: 'https://avatars.githubusercontent.com/u/51393021?v=4',
    },
    id: 6,
    title: '15분회고 프로젝트',
    content: '업데이트 노트 : \n어제 피자를 먹었다. 맛있었다.',
    step: FeedStatus.COMPLETE,
    sos: false,
    thumbnailUrl: 'https://dksykemwl00pf.cloudfront.net/1626853232915dGh1bWJuYWlsSW1hZ2U=image/png',
  },
  {
    author: {
      id: 33,
      nickname: 'Kwon Se-jin',
      imageUrl: 'https://avatars.githubusercontent.com/u/48755175?v=4',
    },
    id: 4,
    title: '하하하하하',
    content: 'dagkasdgdgas\n\n\nsdgagasd',
    step: FeedStatus.PROGRESS,
    sos: true,
    thumbnailUrl:
      'https://dksykemwl00pf.cloudfront.net/1626847909246dGh1bWJuYWlsSW1hZ2U=image/jpeg',
  },
];
