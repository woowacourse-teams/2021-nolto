import React from 'react';

import SubCommentModule from './SubCommentModule';

export default {
  title: 'components/RootCommentModule/RootComment/SubCommentModule',
  component: SubCommentModule,
  argTypes: {},
};

export const Default = () => <SubCommentModule commentId={1} isReplyFormVisible={false} />;
