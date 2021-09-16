import { Feed, FeedDetail, FeedStep, FeedToUpload } from 'types';

interface InfiniteFeedResponse {
  feeds: Feed[];
  nextFeedId: number;
}

export const MOCK_RECENT_FEEDS: InfiniteFeedResponse = {
  feeds: [
    {
      author: {
        id: 46,
        nickname: '신기하제',
        imageUrl: 'https://avatars.githubusercontent.com/u/61370901?v=4',
      },
      id: 116,
      title: '기쁘다',
      content: '기뻐',
      step: FeedStep.PROGRESS,
      sos: false,
      thumbnailUrl: 'https://dksykemwl00pf.cloudfront.net/96fc5ff5d38b4b6f9dbea94172ae4c07.jpg',
    },
    {
      author: {
        id: 46,
        nickname: '신기하제',
        imageUrl: 'https://avatars.githubusercontent.com/u/61370901?v=4',
      },
      id: 115,
      title: '용량',
      content: '테스트',
      step: FeedStep.PROGRESS,
      sos: false,
      thumbnailUrl: 'https://dksykemwl00pf.cloudfront.net/d2bc930630a849b083370388c37ab32d.jpg',
    },
    {
      author: {
        id: 33,
        nickname: '위키미키',
        imageUrl:
          'https://dksykemwl00pf.cloudfront.net/1628560328982S2FrYW9UYWxrXzIwMjEwODA2XzIxMDkxNTQyOC5wbmc=.png',
      },
      id: 114,
      title: '지그 닮은 울릉도 호박엿',
      content: '닮았네요 ~ 💯',
      step: FeedStep.PROGRESS,
      sos: false,
      thumbnailUrl: 'https://dksykemwl00pf.cloudfront.net/84d4739e9acd467ea47af9f4b486f24e.jpg',
    },
    {
      author: {
        id: 51,
        nickname: '닉네밍 🔥',
        imageUrl: 'https://avatars.githubusercontent.com/u/43840561?v=4',
      },
      id: 113,
      title: 'My First Posts뇹',
      content: 'ㄴㅁㅇㄴㅇㅁㄴ',
      step: FeedStep.PROGRESS,
      sos: false,
      thumbnailUrl: 'https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png',
    },
  ],
  nextFeedId: 85,
};

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
  step: FeedStep.PROGRESS,
  sos: false,
  storageUrl: 'https://github.com/zigsong/Ah-Really',
  deployedUrl: 'https://zigsong.github.io/Ah-Really/',
  thumbnailUrl:
    'https://dksykemwl00pf.cloudfront.net/16278104820814YSL4YWh4YSM4YW14Yar4YSN4YWh4YSL4YWtLnBuZw==.png',
  likes: 7,
  views: 97,
  liked: false,
};

export const MOCK_FEED_TO_UPLOAD: FeedToUpload = {
  title: '아진짜요? 토이게임',
  content: '아진짜요? 토이게임 진짜 재밌어요',
  step: FeedStep.PROGRESS,
  sos: false,
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
  storageUrl: 'https://github.com/zigsong/Ah-Really',
  deployedUrl: 'https://zigsong.github.io/Ah-Really/',
  thumbnailImage: null,
};
