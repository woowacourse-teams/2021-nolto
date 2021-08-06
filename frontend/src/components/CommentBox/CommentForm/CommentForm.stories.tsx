import React from 'react';

import CommentForm from './CommentForm';

export default {
  title: 'components/commentBox/CommentForm',
  component: CommentForm,
  argTypes: {},
};

export const ReplyCommentForm = () => (
  <CommentForm
    onSubmit={() => {
      console.log('제출');
    }}
  />
);
export const RootCommentForm = () => (
  <CommentForm
    onSubmit={() => {
      console.log('제출');
    }}
    isRoot={true}
  />
);
