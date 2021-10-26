import React from 'react';
import { Feed, FeedStep } from 'types';
import { MOCK_USER } from '__mocks__/fixture/user';

import RegularFeedCard from './RegularFeedCard';

export default {
  title: 'components/RegularCard',
  component: RegularFeedCard,
  argTypes: {},
};

const mockFeed: Feed = {
  id: 1,
  author: MOCK_USER.ZIG,
  title: 'Good Toy',
  content:
    'Good Nice Perfect Gorgeous Wonderful! Good Nice Perfect Gorgeous Wonderful! Good Nice Perfect Gorgeous Wonderful! Good Nice Perfect Gorgeous Wonderful! ',
  thumbnailUrl: 'https://i.pinimg.com/236x/f5/45/6e/f5456e14993cac65828e289048a89f3e.jpg',
  sos: false,
  step: FeedStep.COMPLETE,
};

export const Default = () => <RegularFeedCard feed={mockFeed} />;
