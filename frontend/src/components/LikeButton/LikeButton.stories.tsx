import React from 'react';

import { MOCK_FEED_DETAIL } from '__mocks__/fixture/Feeds';
import LikeButton from './LikeButton';

export default {
  title: 'components/LikeButton',
  component: LikeButton,
  argTypes: {},
};

export const Default = () => <LikeButton feedDetail={MOCK_FEED_DETAIL} />;
