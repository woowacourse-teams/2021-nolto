import React, { useState } from 'react';

import Avatar from 'components/@common/Avatar/Avatar';
import IconButton from 'components/@common/IconButton/IconButton';
import { refineDate } from 'utils/common';
import { PALETTE } from 'constants/palette';
import ThumbIcon from 'assets/thumb.svg';
import ReplyIcon from 'assets/reply.svg';
import PencilIcon from 'assets/pencil.svg';
import TrashIcon from 'assets/trash.svg';
import { FlexContainer } from 'commonStyles';
import { isRootComment } from 'utils/typeGuard';
import CommentForm from '../CommentForm/CommentForm';
import useMember from 'hooks/queries/useMember';
import Styled, { CommentTextButton, ModifyTextInput } from './Comment.styles';
import { ButtonStyle, CommentBase } from 'types';
import useCommentDelete from 'hooks/queries/comment/useCommentDelete';
import useCommentsModule from 'context/comment/useCommentsModule';
import useSnackBar from 'context/snackBar/useSnackBar';
import useDialog from 'context/dialog/useDialog';

interface Props {
  comment: CommentBase;
}

const Comment = ({ comment }: Props) => {
  const [isReplyToggled, setIsReplyToggled] = useState(false);
  const [isModifying, setIsModifying] = useState(false);
  const member = useMember();
  const snackBar = useSnackBar();
  const dialog = useDialog();
  const { feedId, reloadComment } = useCommentsModule();
  const commentDeleteMutation = useCommentDelete(
    { feedId, commentId: comment.id },
    {
      onSuccess: () => {
        snackBar.addSnackBar('success', '댓글 삭제에 성공했습니다.');
        reloadComment();
      },
    },
  );
  const isMyComment = member.userData?.id === comment.author.id;

  const handleClickReply = () => {
    setIsReplyToggled(!isReplyToggled);
  };

  const handleClickDelete = () => {
    dialog.confirm('정말 댓글을 삭제하시겠습니까?', () => {
      commentDeleteMutation.mutate();
    });
  };

  return (
    <div>
      <Styled.Author>
        <div>
          <Avatar user={comment.author} />
        </div>
        {comment.feedAuthor && <span>작성자</span>}
        {isRootComment(comment) && comment.helper && <span>도와줄게요 🙌</span>}
      </Styled.Author>
      <Styled.Body isModifying={isModifying}>
        <Styled.Content isFeedAuthor={comment.feedAuthor}>
          {isModifying ? (
            <ModifyTextInput type="text" value={comment.content} />
          ) : (
            <span>{comment.content}</span>
          )}
        </Styled.Content>
        <Styled.Detail>
          <FlexContainer gap="4px" alignItems="center">
            <span>{refineDate(comment.createdAt)}</span>
            <Styled.ThumbUpWrapper>
              <IconButton isShadow={false}>
                <ThumbIcon width="20px" />
              </IconButton>
              <span>{comment.likes}</span>
            </Styled.ThumbUpWrapper>
            {isRootComment(comment) && (
              <IconButton onClick={handleClickReply} isShadow={false}>
                <ReplyIcon width="20px" />
              </IconButton>
            )}
          </FlexContainer>
          <FlexContainer gap="4px" alignItems="center">
            {isMyComment &&
              (isModifying ? (
                <>
                  <CommentTextButton buttonStyle={ButtonStyle.SOLID} reverse={true}>
                    수정
                  </CommentTextButton>
                  <CommentTextButton
                    onClick={() => setIsModifying(false)}
                    buttonStyle={ButtonStyle.SOLID}
                    reverse={true}
                  >
                    취소
                  </CommentTextButton>
                </>
              ) : (
                <>
                  <IconButton onClick={() => setIsModifying(true)} isShadow={false}>
                    <PencilIcon width="20px" fill={PALETTE.BLACK_200} />
                  </IconButton>
                  <IconButton onClick={handleClickDelete} isShadow={false}>
                    <TrashIcon width="20px" />
                  </IconButton>
                </>
              ))}
          </FlexContainer>
        </Styled.Detail>
      </Styled.Body>
      {isReplyToggled && (
        <Styled.ReplyFromWrapper>
          <CommentForm />
        </Styled.ReplyFromWrapper>
      )}
    </div>
  );
};

export default Comment;
