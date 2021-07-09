import React from 'react';

import RegularCard from './RegularCard';

export default {
  title: 'components/RegularCard',
  component: RegularCard,
  argTypes: {},
};

const mockFeed = {
  id: 1,
  user: {
    id: 1,
    nickname: 'zigsong',
    imageUrl: 'https://avatars.githubusercontent.com/u/48755175?v=4',
  },
  title: 'Good Toy',
  content: 'Good Nice Perfect Gorgeous Wonderful!',
  thumbnailUrl: 'https://i.pinimg.com/236x/f5/45/6e/f5456e14993cac65828e289048a89f3e.jpg',
  sos: false,
};

export const Default = () => <RegularCard feed={mockFeed} />;
