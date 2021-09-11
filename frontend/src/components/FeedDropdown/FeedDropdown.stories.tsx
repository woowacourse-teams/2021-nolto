import React from 'react';

import FeedDropdown from './FeedDropdown';
import { FeedDetail, FeedStatus } from 'types';

export default {
  title: 'components/FeedDropdown',
  component: FeedDropdown,
  argTypes: {},
};

const feedDetail: FeedDetail = {
  author: {
    id: 30,
    nickname: '뱁새',
    imageUrl: 'https://dksykemwl00pf.cloudfront.net/16282323039854YSH4YWi4Ya44YSJ4YWiLmpwZw==.jpg',
  },
  id: 3,
  title: '깃허브지은',
  techs: [
    {
      id: 187,
      text: 'React Hooks API',
    },
    {
      id: 25,
      text: 'ReactJS',
    },
    {
      id: 187,
      text: 'React Hooks API',
    },
    {
      id: 25,
      text: 'ReactJS',
    },
  ],
  content: '오늘도 열심히 코딩중',
  step: FeedStatus.PROGRESS,
  sos: true,
  storageUrl: 'https://www.aa.bb.com',
  deployedUrl: null,
  thumbnailUrl: 'https://dksykemwl00pf.cloudfront.net/1626847904391dGh1bWJuYWlsSW1hZ2U=image/jpeg',
  likes: 4,
  views: 288,
  liked: false,
};

export const Default = () => <FeedDropdown feedDetail={feedDetail} />;
