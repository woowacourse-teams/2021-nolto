import React from 'react';

import Avatar from './Avatar';

export default {
  title: 'components/common/Avatar',
  component: Avatar,
  argTypes: {},
};

export const Regular = () => (
  <Avatar
    user={{
      id: 1,
      nickname: 'zigsong',
      imageUrl: 'https://avatars.githubusercontent.com/u/48755175?v=4',
    }}
  />
);
