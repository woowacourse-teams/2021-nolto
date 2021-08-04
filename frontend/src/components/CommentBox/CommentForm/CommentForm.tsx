import React from 'react';

import { Author } from 'types';
import SendIcon from 'assets/send.svg';
import { CommentFormInput, Form, SendButton } from './CommentForm.styles';
import Styled from './CommentForm.styles';
import Avatar from 'components/@common/Avatar/Avatar';
import Toggle from 'components/@common/Toggle/Toggle';

interface Props {
  author: Author;
  isRoot?: boolean;
}

const CommentForm = ({ author, isRoot = false }: Props) => {
  return (
    <div>
      <Styled.Author>
        <Avatar user={author} />
      </Styled.Author>
      <Form>
        <Styled.FormInputWrapper>
          <CommentFormInput />
          <SendButton isShadow={false}>
            <SendIcon width="21px" height="21px" />
          </SendButton>
        </Styled.FormInputWrapper>
        {isRoot && (
          <Styled.Help>
            <Toggle labelText="도와줄게요 🙌" />
          </Styled.Help>
        )}
      </Form>
    </div>
  );
};

export default CommentForm;
