import React, { ChangeEvent, useContext, useState } from 'react';

import PencilIcon from 'assets/pencil.svg';
import ReplyIcon from 'assets/reply.svg';
import ThumbIcon from 'assets/thumb.svg';
import TrashIcon from 'assets/trash.svg';
import { FlexContainer } from 'commonStyles';
import Avatar from 'components/@common/Avatar/Avatar';
import IconButton from 'components/@common/IconButton/IconButton';
import { PALETTE } from 'constants/palette';
import useDialog from 'contexts/dialog/useDialog';
import useComment from 'components/CommentModule/useComment';
import useMember from 'hooks/queries/useMember';
import { ButtonStyle, CommentType } from 'types';
import { refineDate } from 'utils/common';
import Styled, { CommentTextButton, ModifyTextInput } from './Comment.styles';
import SubCommentModule from 'components/CommentModule/SubCommentModule/SubCommentModule';
import { CommentModuleContext } from 'components/CommentModule/CommentModule';

interface Props {
  commentBody: CommentType;
  parentCommentId?: number;
}

const Comment = ({ commentBody, parentCommentId }: Props) => {
  const [isReplyFormVisible, setIsReplyFormVisible] = useState(false);
  const [isModifying, setIsModifying] = useState(false);
  const [modifyInput, setModifyInput] = useState('');
  const dialog = useDialog();
  const member = useMember();
  const { feedId } = useContext(CommentModuleContext);
  const comment = useComment({ feedId, commentId: commentBody.id, parentCommentId });

  //TODO: 지금 commentBody.feedAuthor가 로그인된 사용자 정보랑 댓글 작성자랑 같을 때 true가 되고있음
  //로그인된 사용자 정보와 관계없이 피드 작성자 기준으로 feedAuthor가 설정되어야함
  const isMyComment = member.userData?.id === commentBody.author.id;
  const isRootComment = parentCommentId === undefined ? true : false;

  const handleClickReply = () => {
    setIsReplyFormVisible(!isReplyFormVisible);
  };

  const handleChangeModifyInput = (event: ChangeEvent<HTMLInputElement>) => {
    setModifyInput(event.target.value);
  };

  const handleChangeToModifyMode = () => {
    setModifyInput(commentBody.content);
    setIsModifying(true);
  };

  const handleModifyComment = () => {
    comment.modify({
      content: modifyInput,
      helper: isRootComment ? commentBody.helper : false,
    });
    setIsModifying(false);
  };

  const handleClickDelete = () => {
    dialog.confirm('정말 댓글을 삭제하시겠습니까?', () => {
      comment.delete();
    });
  };

  return (
    <Styled.Root>
      <Styled.Author>
        <div>
          <Avatar user={commentBody.author} />
        </div>
        {commentBody.feedAuthor && <span>작성자</span>}
        {isRootComment && commentBody.helper && <span>도와줄게요 🙌</span>}
      </Styled.Author>
      <Styled.Body isModifying={isModifying}>
        <Styled.Content isFeedAuthor={commentBody.feedAuthor}>
          {isModifying ? (
            <form onSubmit={handleModifyComment}>
              <ModifyTextInput type="text" value={modifyInput} onChange={handleChangeModifyInput} />
              <Styled.RightBottomWrapper>
                <CommentTextButton buttonStyle={ButtonStyle.SOLID} reverse={true}>
                  수정
                </CommentTextButton>
                <CommentTextButton
                  type="button"
                  onClick={() => setIsModifying(false)}
                  buttonStyle={ButtonStyle.SOLID}
                  reverse={true}
                >
                  취소
                </CommentTextButton>
              </Styled.RightBottomWrapper>
            </form>
          ) : (
            <>
              <span>{commentBody.content}</span>
              {isMyComment && (
                <Styled.RightBottomWrapper>
                  <IconButton onClick={handleChangeToModifyMode} isShadow={false}>
                    <PencilIcon width="20px" fill={PALETTE.BLACK_200} />
                  </IconButton>
                  <IconButton onClick={handleClickDelete} isShadow={false}>
                    <TrashIcon width="20px" />
                  </IconButton>
                </Styled.RightBottomWrapper>
              )}
            </>
          )}
        </Styled.Content>
        <Styled.Detail>
          <FlexContainer gap="4px" alignItems="center">
            <span>{refineDate(commentBody.createdAt)}</span>
            <Styled.ThumbUpWrapper>
              <IconButton isShadow={false}>
                <ThumbIcon width="20px" />
              </IconButton>
              <span>{commentBody.likes}</span>
            </Styled.ThumbUpWrapper>
            {isRootComment && (
              <IconButton onClick={handleClickReply} isShadow={false}>
                <ReplyIcon width="20px" />
              </IconButton>
            )}
          </FlexContainer>
        </Styled.Detail>
      </Styled.Body>

      {isRootComment && (
        <SubCommentModule
          parentCommentId={commentBody.id}
          isReplyFormVisible={isReplyFormVisible}
        />
      )}
    </Styled.Root>
  );
};

export default Comment;
