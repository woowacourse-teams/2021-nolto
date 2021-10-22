import React, { ChangeEvent, FormEvent, useContext, useState } from 'react';

import SendIcon from 'assets/send.svg';
import Avatar from 'components/@common/Avatar/Avatar';
import Toggle from 'components/@common/Toggle/Toggle';
import { CommentModuleContext } from 'components/CommentModule/CommentModule';
import useMember from 'contexts/member/useMember';
import useFeedDetail from 'hooks/queries/feed/useFeedDetail';
import { MESSAGES } from 'constants/message';
import { CommentRequest } from 'types';
import Styled, { CommentFormInput, Form, SendButton } from './CommentForm.styles';

interface Props {
  onSubmit: ({ content }: CommentRequest) => void;
  isRootComment?: boolean;
}

const CommentForm = ({ onSubmit, isRootComment = false }: Props) => {
  const [content, setContent] = useState('');
  const [isHelper, setIsHelper] = useState(false);
  const { userInfo } = useMember();
  const { feedId } = useContext(CommentModuleContext);
  const { data: feedDetail } = useFeedDetail({ feedId, suspense: false });

  const isMyComment = userInfo?.id === feedDetail?.author.id;

  const handleSubmitComment = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    if (content === '') return;

    setContent('');

    onSubmit({ content, helper: isHelper });
  };

  const handleChangeContent = (event: ChangeEvent<HTMLInputElement>) => {
    setContent(event.target.value);
  };

  const handleChangeHelper = (event: ChangeEvent<HTMLInputElement>) => {
    setIsHelper(event.target.checked);
  };

  return (
    <div>
      {userInfo && (
        <Styled.Author>
          <Avatar user={userInfo} />
        </Styled.Author>
      )}
      <Form onSubmit={handleSubmitComment}>
        <Styled.FormInputWrapper>
          {userInfo ? (
            <CommentFormInput
              aria-label="댓글 작성"
              value={content}
              disabled={false}
              onChange={handleChangeContent}
            />
          ) : (
            <CommentFormInput aria-label="댓글 작성" value={MESSAGES.NEED_LOGIN} disabled={true} />
          )}
          <SendButton size="1.5rem" hasShadow={false} disabled={!userInfo}>
            <span className="visually-hidden">댓글 입력</span>
            <SendIcon width="21px" height="21px" />
          </SendButton>
        </Styled.FormInputWrapper>
        {isRootComment && !isMyComment && (
          <Toggle onChange={handleChangeHelper} checked={isHelper} labelText="도와줄게요 🙌" />
        )}
      </Form>
    </div>
  );
};

export default CommentForm;
