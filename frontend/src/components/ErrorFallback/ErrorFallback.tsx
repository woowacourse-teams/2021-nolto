import React, { useEffect } from 'react';
import { useQueryClient } from 'react-query';

import catError from 'assets/catError.png';
import Styled from './ErrorFallback.styles';

interface Props {
  message: string;
  queryKey?: string;
}

const ErrorFallback = ({ message, queryKey }: Props) => {
  const queryClient = useQueryClient();

  useEffect(() => {
    queryClient.resetQueries(queryKey && queryKey);
  }, []);

  return (
    <Styled.Root>
      <Styled.Image width="480px" src={catError} alt="error" />
      <Styled.Message>
        <Styled.ErrorText>ERROR</Styled.ErrorText>
        <Styled.ErrorDetail>{message}</Styled.ErrorDetail>
      </Styled.Message>
    </Styled.Root>
  );
};

export default ErrorFallback;
