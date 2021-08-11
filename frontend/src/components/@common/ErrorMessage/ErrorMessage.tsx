import React from 'react';
import { FieldError } from 'react-hook-form';

import Styled from './ErrorMessage.styles';

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
      <Styled.ArrowUpIcon width="1rem" />
      <Styled.Message>
        <span>{targetError.message}</span>
      </Styled.Message>
    </Styled.Root>
  );
};

export default ErrorMessage;
