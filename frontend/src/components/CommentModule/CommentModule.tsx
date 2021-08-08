import React, { useMemo } from 'react';

import { CommentRequest } from 'types';
import Styled from './CommentModule.styles';
import Comment from './@common/Comment/Comment';
import CommentForm from './@common/CommentForm/CommentForm';
import useCommentModule from './useCommentModule';

interface Props {
  feedId: number;
}

interface CommentsContext {
  feedId: number;
}

export const CommentModuleContext = React.createContext<CommentsContext>(null);

const CommentModule = ({ feedId }: Props) => {
  const commentModule = useCommentModule({ feedId });

  const handleSubmitComment = (commentRequest: CommentRequest) => {
    commentModule.write(commentRequest);
  };

  const commentsContext = useMemo(() => ({ feedId }), []);

  return (
    <CommentModuleContext.Provider value={commentsContext}>
      <div>
        <h3>댓글 {commentModule.data.length}개</h3>
        <hr />
        <Styled.CommentContainer>
          <CommentForm onSubmit={handleSubmitComment} isRootComment={true} />
          {commentModule.data.map((comment) => (
            <Comment key={comment.id} commentBody={comment} />
          ))}
        </Styled.CommentContainer>
      </div>
    </CommentModuleContext.Provider>
  );
};

export default CommentModule;
