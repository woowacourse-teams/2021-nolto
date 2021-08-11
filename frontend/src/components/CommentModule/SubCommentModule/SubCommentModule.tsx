import React, { useContext, useState } from 'react';
import Styled, { FoldButton, ReplyIconButton } from './SubCommentModule.styles';

import useCommentModule from 'components/CommentModule/useCommentModule';
import ReplyIcon from 'assets/reply.svg';
import { CommentRequest } from 'types';
import Comment from 'components/CommentModule/@common/Comment/Comment';
import CommentForm from 'components/CommentModule/@common/CommentForm/CommentForm';
import { CommentModuleContext } from '../CommentModule';

interface Props {
  parentCommentId: number;
}

const SubCommentModule = ({ parentCommentId }: Props) => {
  const [isReplyFormVisible, setIsReplyFormVisible] = useState(false);
  const [isFold, setIsFold] = useState(false);
  const { feedId } = useContext(CommentModuleContext);
  const subCommentModule = useCommentModule({ feedId, parentCommentId });
  const isSubCommentExist = subCommentModule.data?.length > 0;

  const handleClickReply = () => {
    if (isFold) {
      setIsFold(false);
      setIsReplyFormVisible(true);
      return;
    }

    setIsReplyFormVisible(!isReplyFormVisible);
  };

  const handleClickFold = () => {
    setIsFold(!isFold);
  };

  const handleWriteSubComment = (subCommentRequest: CommentRequest) => {
    if (subCommentRequest.content === '') return;

    subCommentModule.write(subCommentRequest);
  };

  return (
    <Styled.Root>
      <ReplyIconButton onClick={handleClickReply} isShadow={false}>
        <ReplyIcon width="20px" />
      </ReplyIconButton>
      {isSubCommentExist && (
        <FoldButton onClick={handleClickFold}>
          <Styled.ArrowUp isFold={isFold} width="10px" />
          {`답글 ${subCommentModule.data.length}개 ${isFold ? '더보기' : '숨기기'}`}
        </FoldButton>
      )}

      <Styled.SubCommentWrapper
        isFold={isFold}
        isReplyFormVisible={isReplyFormVisible}
        replyCount={isSubCommentExist ? subCommentModule.data.length : 0}
      >
        {isReplyFormVisible && <CommentForm onSubmit={handleWriteSubComment} />}
        {isSubCommentExist &&
          subCommentModule.data.map((subComment) => (
            <Comment
              key={subComment.id}
              commentBody={subComment}
              parentCommentId={parentCommentId}
            />
          ))}
      </Styled.SubCommentWrapper>
    </Styled.Root>
  );
};

export default SubCommentModule;
