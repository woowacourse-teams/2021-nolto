import React from 'react';

import Thumbnail from './Thumbnail';

export default {
  title: 'components/FeedThumbnail',
  component: Thumbnail,
  argTypes: {},
};

export const Default = () => (
  <Thumbnail
    thumbnailUrl={'https://i.pinimg.com/236x/f5/45/6e/f5456e14993cac65828e289048a89f3e.jpg'}
  />
);
