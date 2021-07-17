import React from 'react';
import { FieldError } from 'react-hook-form';

import Styled from './ErrorMessage.styles';
import { PALETTE } from 'constants/palette';

interface Props {
  targetError: FieldError;
  className?: string;
}

const ErrorMessage = ({ targetError, className }: Props) => {
  if (!targetError?.type) {
    return null;
  }

  return (
    <Styled.Root className={className}>
      <Styled.ArrowUpWrapper>
        <Styled.ArrowUp width="0.75rem" />
      </Styled.ArrowUpWrapper>
      <Styled.Message>
        <Styled.Background></Styled.Background>
        <span>{`${targetError.message}`}</span>
      </Styled.Message>
    </Styled.Root>
  );
};

export default ErrorMessage;
