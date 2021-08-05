import { Feed, FeedDetail, FeedStatus } from 'types';

export const MOCK_FEEDS: Feed[] = [
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

export const MOCK_FEED_DETAIL: FeedDetail = {
  author: {
    id: 7,
    nickname: 'zigsong',
    imageUrl: 'https://avatars.githubusercontent.com/u/44080404?v=4',
  },
  id: 3,
  title: '아진짜요? 토이게임',
  techs: [
    {
      id: 655,
      text: 'JavaScript',
    },
    {
      id: 983,
      text: 'HTML',
    },
    {
      id: 1243,
      text: 'ML Kit',
    },
  ],
  content:
    '2020년에 p5js와 ml로 만든 프로젝트입니다\n제가 팀장이었어요\n저만 일했어요\n우테코에서는 함께 일해서 행복해요!',
  step: FeedStatus.PROGRESS,
  sos: false,
  storageUrl: 'https://github.com/zigsong/Ah-Really',
  deployedUrl: 'https://zigsong.github.io/Ah-Really/',
  thumbnailUrl:
    'https://dksykemwl00pf.cloudfront.net/16278104820814YSL4YWh4YSM4YW14Yar4YSN4YWh4YSL4YWtLnBuZw==.png',
  likes: 7,
  views: 97,
  liked: false,
};
