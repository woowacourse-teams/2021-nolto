import React from 'react';

import CommentModule from './CommentModule';

export default {
  title: 'components/CommentModule/CommentModule',
  component: CommentModule,
  argTypes: {},
};

export const Default = () => <CommentModule feedId={1} />;
