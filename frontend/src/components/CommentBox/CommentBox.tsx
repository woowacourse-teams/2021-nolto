import React from 'react';

import { RootComment } from 'types';
import Comment from 'components/CommentBox/Comment/Comment';
import RepliesProvider from 'context/comment/reply/RepliesProvider';

interface Props {
  commentBoxInfo: RootComment;
}

const CommentBox = ({ commentBoxInfo }: Props) => {
  const { id: commentId } = commentBoxInfo;

  return (
    <div>
      <RepliesProvider commentId={commentId}>
        <Comment comment={commentBoxInfo} />
      </RepliesProvider>
    </div>
  );
};

export default CommentBox;
