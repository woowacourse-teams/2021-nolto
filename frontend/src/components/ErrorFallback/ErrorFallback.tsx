import React, { useEffect } from 'react';
import { useQueryClient } from 'react-query';

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
      <Styled.Message>
        <Styled.ErrorTitle>Oops!</Styled.ErrorTitle>
        <Styled.Horse />
      </Styled.Message>
      <Styled.Divider />
      <Styled.ErrorText>ERROR | {message}</Styled.ErrorText>
    </Styled.Root>
  );
};

export default ErrorFallback;
