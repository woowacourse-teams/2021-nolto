import React from 'react';
import { QueryClient, QueryClientProvider } from 'react-query';

import { ErrorHandler } from 'types';
import CustomError from 'utils/CustomError';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: false,
    },
  },
});

interface Props {
  children: React.ReactNode;
}

export const wrapper = ({ children }: Props) => (
  <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
);

export const errorHandler: ErrorHandler = (error: CustomError) => console.error(error);
