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

export const MOCK_HOT_FEEDS: Feed[] = [
  {
    author: {
      id: 48,
      nickname: '신지혜',
      imageUrl: 'https://dksykemwl00pf.cloudfront.net/151ac941c4f54908b48f2818105d1042.gif',
    },
    id: 22,
    title: '개쩌는 마찌의 지하철 미션',
    content: '기깔나죠?',
    step: FeedStep.PROGRESS,
    sos: true,
    thumbnailUrl: 'https://dksykemwl00pf.cloudfront.net/2072d13e229e47bca2879d431ec2409c.gif',
  },
  {
    author: {
      id: 56,
      nickname: '공원',
      imageUrl:
        'https://dksykemwl00pf.cloudfront.net/1628583848691NTcxNDM2MjE2MF84ZjhhZjFjMTBkX2suanBn.jpg',
    },
    id: 81,
    title: '샘플 프로젝트',
    content:
      '지그가 이랬다저랬다 하지만 순순히 등록을 해보았습니다. \n포맷팅도 추가해줬으면 좋겠다.',
    step: FeedStep.PROGRESS,
    sos: false,
    thumbnailUrl:
      'https://dksykemwl00pf.cloudfront.net/16285835170854YSL4YWl4Yar4YSD4YWl4YSD4YWl4YSK4YW14pmq4pmq4pmq4pmq4pmq4pmqLmpwZw==.jpg',
  },
  {
    author: {
      id: 44,
      nickname: 'ㅋㅋ맨',
      imageUrl:
        'https://dksykemwl00pf.cloudfront.net/1628566572705MTU1NTk5ODI0OV8zdnFIUGJnYTBUZjVFenkwTVFPeThZV3hiUU10QVVVdHdJZVVhUmVQLmpwZWc=.jpeg',
    },
    id: 77,
    title: '숨참기 프로젝트',
    content: '숨을 얼마나 오래 참을 수있는지 확인할수 있는 프로젝트예요',
    step: FeedStep.PROGRESS,
    sos: true,
    thumbnailUrl:
      'https://dksykemwl00pf.cloudfront.net/1628565773019M+GEjuGFoV/hhIPhhabhhIbhhanhhIPhhabhhIvhhbVf4YSL4YW14Yar4YSR4YWz4YSF4YWhX+GEi+GFoeGEj+GFteGEkOGFpuGGqOGEjuGFpS5wbmc=.png',
  },
  {
    author: {
      id: 55,
      nickname: "'where 1=",
      imageUrl:
        'https://dksykemwl00pf.cloudfront.net/16285637584914YSJ4YWz4YSP4YWz4YSF4YW14Yar4YSJ4YWj4Ya6IDIwMjEtMDgtMDEg4YSL4YWp4YSS4YWuIDMuNTQuNDIucG5n.png',
    },
    id: 76,
    title: '자바 네트워크 프로그래밍',
    content: '네트워크 프로그래밍\n채팅앱도 만들고\nDNS도 만들고\nHTTP 서버도 만들고\n굿굿굿\n',
    step: FeedStep.PROGRESS,
    sos: false,
    thumbnailUrl:
      'https://dksykemwl00pf.cloudfront.net/16285634058034YSJ4YWz4YSP4YWz4YSF4YW14Yar4YSJ4YWj4Ya6IDIwMjEtMDgtMDIg4YSL4YWp4YSS4YWuIDEwLjE4LjE5LnBuZw==.png',
  },
  {
    author: {
      id: 46,
      nickname: '신기하제',
      imageUrl: 'https://avatars.githubusercontent.com/u/61370901?v=4',
    },
    id: 20,
    title: '조엘의 웹 호스팅',
    content:
      '개발자들이 HTML, CSS, JS 파일을 첨부하면 하나의 페이지를 만들어주는 토이 프로젝트를 진행했어요!\n\n사용자는 로그인 후 하나의 HTML 파일, 그리고 이를 꾸미는 CSS, JS 파일을 첨부하여 하나의 페이지를 받아볼 수 있어요!',
    step: FeedStep.COMPLETE,
    sos: false,
    thumbnailUrl:
      'https://dksykemwl00pf.cloudfront.net/1627525457136am9lbC13ZWItaG9zdGluZy5zdmc=.svg',
  },
  {
    author: {
      id: 33,
      nickname: '위키미키',
      imageUrl:
        'https://dksykemwl00pf.cloudfront.net/1628560328982S2FrYW9UYWxrXzIwMjEwODA2XzIxMDkxNTQyOC5wbmc=.png',
    },
    id: 4,
    title: '하하하하하',
    content: 'dagkasdgdgas\n\n\nsdgagasd',
    step: FeedStep.PROGRESS,
    sos: true,
    thumbnailUrl: 'https://dksykemwl00pf.cloudfront.net/8c446f4ad4e44c5cb6a9913c9558dec2.jpeg',
  },
  {
    author: {
      id: 40,
      nickname: 'Gomding',
      imageUrl: 'https://dksykemwl00pf.cloudfront.net/691aa273e8e149338c0a67d574184c2e.jpeg',
    },
    id: 23,
    title: '우리집 고양이 봐주세요..',
    content: '귀엽죠?',
    step: FeedStep.PROGRESS,
    sos: true,
    thumbnailUrl: 'https://dksykemwl00pf.cloudfront.net/e035983f37f54b419c71135d58bfded2.jpeg',
  },
  {
    author: {
      id: 44,
      nickname: 'ㅋㅋ맨',
      imageUrl:
        'https://dksykemwl00pf.cloudfront.net/1628566572705MTU1NTk5ODI0OV8zdnFIUGJnYTBUZjVFenkwTVFPeThZV3hiUU10QVVVdHdJZVVhUmVQLmpwZWc=.jpeg',
    },
    id: 6,
    title: '15분회고 프로젝트',
    content: '업데이트 노트 : \n어제 피자를 먹었다. 맛있었다.',
    step: FeedStep.COMPLETE,
    sos: false,
    thumbnailUrl: 'https://dksykemwl00pf.cloudfront.net/1626853232915dGh1bWJuYWlsSW1hZ2U=image/png',
  },
  {
    author: {
      id: 30,
      nickname: '뱁새',
      imageUrl:
        'https://dksykemwl00pf.cloudfront.net/16282323039854YSH4YWi4Ya44YSJ4YWiLmpwZw==.jpg',
    },
    id: 3,
    title: '깃허브지은',
    content: '오늘도 열심히 코딩중',
    step: FeedStep.PROGRESS,
    sos: true,
    thumbnailUrl:
      'https://dksykemwl00pf.cloudfront.net/1626847904391dGh1bWJuYWlsSW1hZ2U=image/jpeg',
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
