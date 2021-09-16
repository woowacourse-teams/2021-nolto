import React from 'react';

import FeedThumbnail from './FeedThumbnail';

export default {
  title: 'components/FeedThumbnail',
  component: FeedThumbnail,
  argTypes: {},
};

export const Default = () => (
  <FeedThumbnail
    thumbnailUrl={'https://i.pinimg.com/236x/f5/45/6e/f5456e14993cac65828e289048a89f3e.jpg'}
  />
);
