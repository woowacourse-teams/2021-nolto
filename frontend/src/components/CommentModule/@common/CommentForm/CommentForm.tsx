import React, { ChangeEvent, FormEvent, useContext, useState } from 'react';

import SendIcon from 'assets/send.svg';
import Styled, { CommentFormInput, Form, SendButton } from './CommentForm.styles';
import Avatar from 'components/@common/Avatar/Avatar';
import Toggle from 'components/@common/Toggle/Toggle';
import { CommentModuleContext } from 'components/CommentModule/CommentModule';
import useMember from 'hooks/queries/useMember';
import useFeedDetail from 'hooks/queries/feed/useFeedDetail';
import { CommentRequest } from 'types';

interface Props {
  onSubmit: ({ content }: CommentRequest) => void;
  isRootComment?: boolean;
}

const CommentForm = ({ onSubmit, isRootComment = false }: Props) => {
  const [content, setContent] = useState('');
  const [isHelper, setIsHelper] = useState(false);
  const { userData, isLogin } = useMember();
  const { feedId } = useContext(CommentModuleContext);
  const { data: feedDetail } = useFeedDetail({ feedId, suspense: false });
  const member = useMember();

  const isMyComment = member.userData?.id === feedDetail?.author.id;

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
        {isRootComment && !isMyComment && (
          <Toggle onChange={handleChangeHelper} checked={isHelper} labelText="도와줄게요 🙌" />
        )}
      </Form>
    </div>
  );
};

export default CommentForm;
