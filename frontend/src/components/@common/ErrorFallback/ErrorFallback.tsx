import React from 'react';

import catError from 'assets/catError.jpeg';
import Styled from './ErrorFallback.styles';

interface Props {
  message: string;
}

const ErrorFallback = ({ message }: Props) => {
  return (
    <Styled.Root>
      <Styled.ErrorText>ERROR</Styled.ErrorText>
      <img src={catError} alt="error" />
      <Styled.Message>{message}</Styled.Message>
    </Styled.Root>
  );
};

export default ErrorFallback;
