import React from 'react';

import SubCommentModule from './SubCommentModule';

export default {
  title: 'components/CommentModule/SubCommentModule',
  component: SubCommentModule,
  argTypes: {},
};

export const Default = () => <SubCommentModule parentCommentId={2} isReplyFormVisible={true} />;
