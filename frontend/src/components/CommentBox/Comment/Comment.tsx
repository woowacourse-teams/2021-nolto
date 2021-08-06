import React, { ChangeEvent, useState } from 'react';

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
import { ButtonStyle, CommentBase, CommentRequest } from 'types';
import useCommentDelete from 'hooks/queries/comment/useCommentDelete';
import useCommentsModule from 'context/comment/useCommentsModule';
import useDialog from 'context/dialog/useDialog';
import useCommentModify from 'hooks/queries/comment/useCommentModify';
import useReplyWrite from 'hooks/queries/comment/reply/useReplyWrite';
import useRepliesProvider from 'context/comment/reply/useRepliesProvider';

interface Props {
  comment: CommentBase;
}

const Comment = ({ comment }: Props) => {
  const [isReplyToggled, setIsReplyToggled] = useState(false);
  const [isModifying, setIsModifying] = useState(false);
  const [modifyInput, setModifyInput] = useState('');
  const member = useMember();
  const dialog = useDialog();
  const { feedId, reloadComments } = useCommentsModule();
  const { reloadReplies } = useRepliesProvider();
  const replyWriteMutation = useReplyWrite(
    { feedId, commentId: comment.id },
    {
      onSuccess: () => {
        reloadReplies();
      },
    },
  );
  const commentModifyMutation = useCommentModify(
    { feedId, commentId: comment.id },
    {
      onSuccess: () => {
        isRootComment(comment) ? reloadComments() : reloadReplies();
      },
      //TODO: 에러처리 해야됨
    },
  );
  const commentDeleteMutation = useCommentDelete(
    { feedId, commentId: comment.id },
    {
      onSuccess: () => {
        isRootComment(comment) ? reloadComments() : reloadReplies();
      },
    },
  );
  const isMyComment = member.userData?.id === comment.author.id;

  const handleClickReply = () => {
    setIsReplyToggled(!isReplyToggled);
  };

  const handleModifyComment = () => {
    commentModifyMutation.mutate({
      content: modifyInput,
      helper: isRootComment(comment) && comment.helper,
    });
    setIsModifying(false);
  };

  const handleChangeModifyInput = (event: ChangeEvent<HTMLInputElement>) => {
    setModifyInput(event.target.value);
  };

  const handleChangeToModifyMode = () => {
    setModifyInput(comment.content);
    setIsModifying(true);
  };

  const handleClickDelete = () => {
    dialog.confirm('정말 댓글을 삭제하시겠습니까?', () => {
      commentDeleteMutation.mutate();
    });
  };

  const handleSubmitReply = (commentRequest: CommentRequest) => {
    replyWriteMutation.mutate(commentRequest);
  };

  return (
    <Styled.Root>
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
              <span>{comment.content}</span>
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
        </Styled.Detail>
      </Styled.Body>
      {isReplyToggled && (
        <Styled.ReplyFromWrapper>
          <CommentForm onSubmit={handleSubmitReply} />
        </Styled.ReplyFromWrapper>
      )}
    </Styled.Root>
  );
};

export default Comment;
