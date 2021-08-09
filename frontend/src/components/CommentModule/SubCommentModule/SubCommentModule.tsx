import React, { useContext, useState } from 'react';
import Styled, { FoldButton } from './SubCommentModule.styles';

import useCommentModule from 'components/CommentModule/useCommentModule';
import { CommentRequest } from 'types';
import Comment from 'components/CommentModule/@common/Comment/Comment';
import CommentForm from 'components/CommentModule/@common/CommentForm/CommentForm';
import { CommentModuleContext } from '../CommentModule';

interface Props {
  parentCommentId: number;
  isReplyFormVisible: boolean;
}

const SubCommentModule = ({ isReplyFormVisible, parentCommentId }: Props) => {
  const [isFold, setIsFold] = useState(false);
  const { feedId } = useContext(CommentModuleContext);
  const subCommentModule = useCommentModule({ feedId, parentCommentId });

  const handleClickFold = () => {
    setIsFold(!isFold);
  };

  const handleWriteSubComment = (subCommentRequest: CommentRequest) => {
    subCommentModule.write(subCommentRequest);
  };

  return (
    <Styled.Root>
      {isReplyFormVisible && <CommentForm onSubmit={handleWriteSubComment} />}

      {subCommentModule.data?.length > 0 && (
        <>
          <FoldButton onClick={handleClickFold}>
            <Styled.ArrowUp isFold={isFold} width="10px" />
            {`답글 ${subCommentModule.data.length}개 ${isFold ? '더보기' : '숨기기'}`}
          </FoldButton>

          <Styled.SubCommentWrapper isFold={isFold} replyCount={subCommentModule.data.length}>
            {subCommentModule.data.map((subComment) => (
              <Comment
                key={subComment.id}
                commentBody={subComment}
                parentCommentId={parentCommentId}
              />
            ))}
          </Styled.SubCommentWrapper>
        </>
      )}
    </Styled.Root>
  );
};

export default SubCommentModule;
