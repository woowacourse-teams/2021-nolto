import React from 'react';

import Comment from './Comment';
import { MOCK_COMMENTS } from '__mocks__/fixture/Comments';

export default {
  title: 'components/CommentModule/Comment',
  component: Comment,
  argTypes: {},
};

export const Comment = () => <Comment commentBody={MOCK_COMMENTS[0]} />;
export const SubComment = () => <Comment parentCommentId={1} commentBody={MOCK_COMMENTS[0]} />;
