import React from 'react';

import Comment from './Comment';
import { MOCK_COMMENTS } from '__mocks__/fixture/comments';

export default {
  title: 'components/CommentModule/Comment',
  component: Comment,
  argTypes: {},
};

export const DefaultComment = () => <Comment commentBody={MOCK_COMMENTS[0]} />;
export const SubComment = () => <Comment parentCommentId={1} commentBody={MOCK_COMMENTS[0]} />;
