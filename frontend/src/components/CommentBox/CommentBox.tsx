import React, { useState } from 'react';

import { RootComment } from 'types';
import Comment from 'components/CommentBox/Comment/Comment';
import Styled, { FoldButton } from './CommentBox.styles';

interface Props {
  commentBoxInfo: RootComment;
}

const CommentBox = ({ commentBoxInfo }: Props) => {
  const [isFold, setIsFold] = useState(false);

  const handleClickFold = () => {
    setIsFold(!isFold);
  };

  return (
    <div>
      <Comment comment={commentBoxInfo} />
      <Styled.Reply>
        <FoldButton onClick={handleClickFold}>
          <Styled.ArrowUp isFold={isFold} width="10px" />
          {`답글 ${commentBoxInfo.replies.length}개 ${isFold ? '더보기' : '숨기기'}`}
        </FoldButton>
        <Styled.ReplyWrapper isFold={isFold} replyCount={commentBoxInfo.replies.length}>
          {commentBoxInfo.replies.map((reply) => (
            <Comment comment={reply} />
          ))}
        </Styled.ReplyWrapper>
      </Styled.Reply>
    </div>
  );
};

export default CommentBox;
