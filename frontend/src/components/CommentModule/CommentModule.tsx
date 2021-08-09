import React, { useMemo, useState } from 'react';

import { CommentRequest } from 'types';
import Styled from './CommentModule.styles';
import Comment from './@common/Comment/Comment';
import CommentForm from './@common/CommentForm/CommentForm';
import useCommentModule from './useCommentModule';

interface Props {
  feedId: number;
  focusedCommentId: number;
}

interface CommentsContext {
  feedId: number;
  addCommentCount: (count: number) => void;
}

export const CommentModuleContext = React.createContext<CommentsContext>(null);

const CommentModule = ({ feedId, focusedCommentId }: Props) => {
  const commentModule = useCommentModule({ feedId });
  const [commentCount, setCommentCount] = useState(0);

  const handleSubmitComment = (commentRequest: CommentRequest) => {
    commentModule.write(commentRequest);
  };

  const addCommentCount = (count: number) => {
    setCommentCount((prevCount) => prevCount + count);
  };

  const commentsContext = useMemo(() => ({ feedId, addCommentCount }), []);

  return (
    <CommentModuleContext.Provider value={commentsContext}>
      <div>
        <h3>댓글 {commentCount}개</h3>
        <hr />
        <Styled.CommentContainer>
          <CommentForm onSubmit={handleSubmitComment} isRootComment={true} />
          {commentModule.data.map((comment) => (
            <Comment
              key={comment.id}
              commentBody={comment}
              isFocused={comment.id === focusedCommentId}
            />
          ))}
        </Styled.CommentContainer>
      </div>
    </CommentModuleContext.Provider>
  );
};

export default CommentModule;
