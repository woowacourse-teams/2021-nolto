import React from 'react';

import StretchCard from './StretchCard';
import { Feed, FeedStatus } from 'types';

export default {
  title: 'components/StretchCard',
  component: StretchCard,
  argTypes: {},
};

const mockFeed: Feed = {
  id: 1,
  author: {
    id: 1,
    nickname: 'zigsong',
    imageUrl: 'https://avatars.githubusercontent.com/u/48755175?v=4',
  },
  title: 'Good Toy',
  content: 'Good Nice Perfect Gorgeous Wonderful!',
  thumbnailUrl: 'https://i.pinimg.com/236x/f5/45/6e/f5456e14993cac65828e289048a89f3e.jpg',
  sos: false,
  step: FeedStatus.PROGRESS,
};

export const Default = () => <StretchCard feed={mockFeed} />;
