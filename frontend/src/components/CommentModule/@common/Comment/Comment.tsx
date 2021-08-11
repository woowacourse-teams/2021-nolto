import React, { useContext, useEffect, useState, useRef, FormEvent } from 'react';

import PencilIcon from 'assets/pencil.svg';
import ThumbIcon from 'assets/thumb.svg';
import TrashIcon from 'assets/trash.svg';
import { FlexContainer } from 'commonStyles';
import Avatar from 'components/@common/Avatar/Avatar';
import IconButton from 'components/@common/IconButton/IconButton';
import useComment from 'components/CommentModule/useComment';
import SubCommentModule from 'components/CommentModule/SubCommentModule/SubCommentModule';
import { CommentModuleContext } from 'components/CommentModule/CommentModule';
import useSnackbar from 'contexts/snackbar/useSnackbar';
import useDialog from 'contexts/dialog/useDialog';
import useMember from 'hooks/queries/useMember';
import useLike from 'hooks/@common/useLike';
import { PALETTE } from 'constants/palette';
import { ButtonStyle, CommentType } from 'types';
import { refineDate } from 'utils/common';
import Styled, { CommentTextButton, ModifyTextArea } from './Comment.styles';

interface Props {
  commentBody: CommentType;
  parentCommentId?: number;
  isFocused?: boolean;
}

const Comment = ({ commentBody, parentCommentId, isFocused }: Props) => {
  const [isModifying, setIsModifying] = useState(false);
  const [modifyInput, setModifyInput] = useState('');

  const scrollRef = useRef(null);

  const dialog = useDialog();
  const snackbar = useSnackbar();
  const member = useMember();

  const { feedId, addCommentCount } = useContext(CommentModuleContext);
  const comment = useComment({ feedId, commentId: commentBody.id, parentCommentId });
  const { setLiked, isLiked, likeCount } = useLike({
    initialIsLiked: commentBody.liked,
    likeCount: commentBody.likes,
  });

  //TODO: 지금 commentBody.feedAuthor가 로그인된 사용자 정보랑 댓글 작성자랑 같을 때 true가 되고있음
  //로그인된 사용자 정보와 관계없이 피드 작성자 기준으로 feedAuthor가 설정되어야함
  const isMyComment = member.userData?.id === commentBody.author.id;
  const isRootComment = parentCommentId === undefined ? true : false;

  const handleToggleLiked = () => {
    if (!member.userData) {
      snackbar.addSnackbar('error', '로그인이 필요한 서비스입니다.');
      return;
    }

    if (isLiked) {
      //TODO: 첫번째 인자로 null 넣는 것이 어색함
      comment.unlike(null, {
        onSuccess: () => {
          setLiked(false);
        },
      });

      return;
    }

    comment.like(null, {
      onSuccess: () => {
        setLiked(true);
      },
    });
  };

  const handleChangeModifyInput = (inputValue: string) => {
    setModifyInput(inputValue);
  };

  const handleClickModifyMode = () => {
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

  useEffect(() => {
    if (isFocused) scrollRef.current.scrollIntoView({ behavior: 'smooth' });
  }, [isFocused]);

  useEffect(() => {
    addCommentCount(1);

    return () => {
      addCommentCount(-1);
    };
  }, []);

  const modifyingMode: React.ReactNode = (
    <>
      <ModifyTextArea initialValue={modifyInput} onChange={handleChangeModifyInput} />
      <Styled.EditDeleteContainer>
        <CommentTextButton
          onClick={handleModifyComment}
          type="button"
          buttonStyle={ButtonStyle.SOLID}
          reverse={true}
        >
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
      </Styled.EditDeleteContainer>
    </>
  );

  const exhibitMode: React.ReactNode = (
    <>
      <Styled.ExhibitText>{commentBody.content}</Styled.ExhibitText>
      {commentBody.modified && <span className="modified-text"> (수정됨)</span>}
      {isMyComment && (
        <Styled.EditDeleteContainer>
          <IconButton onClick={handleClickModifyMode} isShadow={false}>
            <PencilIcon width="20px" fill={PALETTE.BLACK_200} />
          </IconButton>
          <IconButton onClick={handleClickDelete} isShadow={false}>
            <TrashIcon width="20px" />
          </IconButton>
        </Styled.EditDeleteContainer>
      )}
    </>
  );

  return (
    <Styled.Root ref={scrollRef}>
      <Styled.Author>
        <div>
          <Avatar user={commentBody.author} />
        </div>
        {commentBody.feedAuthor && <span>작성자</span>}
        {isRootComment && commentBody.helper && <span>도와줄게요 🙌</span>}
      </Styled.Author>
      <Styled.Body isModifying={isModifying}>
        <Styled.Content isFeedAuthor={commentBody.feedAuthor}>
          {isModifying ? modifyingMode : exhibitMode}
        </Styled.Content>
        <Styled.Detail>
          <FlexContainer gap="4px" alignItems="center">
            <span>{refineDate(commentBody.createdAt)}</span>
            <Styled.ThumbUpWrapper>
              <IconButton onClick={handleToggleLiked} isShadow={false}>
                <ThumbIcon fill={isLiked ? PALETTE.GRAY_500 : 'none'} width="20px" />
              </IconButton>
              <span>{likeCount}</span>
            </Styled.ThumbUpWrapper>
          </FlexContainer>
        </Styled.Detail>
      </Styled.Body>

      {isRootComment && <SubCommentModule parentCommentId={commentBody.id} />}
    </Styled.Root>
  );
};

export default Comment;
