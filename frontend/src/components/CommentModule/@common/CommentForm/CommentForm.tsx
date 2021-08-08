import React, { ChangeEvent, FormEvent, useState } from 'react';

import SendIcon from 'assets/send.svg';
import Styled, { CommentFormInput, Form, SendButton } from './CommentForm.styles';
import Avatar from 'components/@common/Avatar/Avatar';
import Toggle from 'components/@common/Toggle/Toggle';
import useMember from 'hooks/queries/useMember';
import { CommentRequest } from 'types';

interface Props {
  onSubmit: ({ content }: CommentRequest) => void;
  isRootComment?: boolean;
}

const CommentForm = ({ onSubmit, isRootComment = false }: Props) => {
  const [content, setContent] = useState('');
  const [isHelper, setIsHelper] = useState(false);
  const { userData, isLogin } = useMember();

  const handleSubmitComment = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
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
      {isLogin && (
        <Styled.Author>
          <Avatar user={userData} />
        </Styled.Author>
      )}
      <Form onSubmit={handleSubmitComment}>
        <Styled.FormInputWrapper>
          {isLogin ? (
            <CommentFormInput value={content} disabled={false} onChange={handleChangeContent} />
          ) : (
            <CommentFormInput value="로그인이 필요한 서비스입니다." disabled={true} />
          )}
          <SendButton isShadow={false} disabled={!isLogin}>
            <SendIcon width="21px" height="21px" />
          </SendButton>
        </Styled.FormInputWrapper>
        {isRootComment && (
          <Styled.Help>
            <Toggle onChange={handleChangeHelper} checked={isHelper} labelText="도와줄게요 🙌" />
          </Styled.Help>
        )}
      </Form>
    </div>
  );
};

export default CommentForm;
