import React from 'react';
import { QueryClient, QueryClientProvider } from 'react-query';
import { MemoryRouter } from 'react-router-dom';
import { render, RenderOptions } from '@testing-library/react';
import { renderHook } from '@testing-library/react-hooks';
import { RenderHookOptions } from '@testing-library/react-hooks/lib/types';

import DialogProvider from 'context/dialog/DialogProvider';
import ModalProvider from 'context/modal/ModalProvider';
import SnackBarProvider from 'context/snackBar/SnackBarProvider';
import AsyncBoundary from 'components/AsyncBoundary';
import ErrorFallback from 'components/ErrorFallback/ErrorFallback';

interface WrapperProps {
  children?: React.ReactNode;
}

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      useErrorBoundary: true,
      suspense: true,
      retry: false,
    },
  },
});

const Wrapper = ({ children }: WrapperProps) => {
  return (
    <QueryClientProvider client={queryClient}>
      <SnackBarProvider>
        <DialogProvider>
          <ModalProvider>
            <MemoryRouter>
              <AsyncBoundary rejectedFallback={<ErrorFallback message="테스트 에러" />}>
                {children}
              </AsyncBoundary>
            </MemoryRouter>
          </ModalProvider>
        </DialogProvider>
      </SnackBarProvider>
    </QueryClientProvider>
  );
};

const customRender = <T extends unknown>(
  ui: React.ReactElement<T, string | React.JSXElementConstructor<T>>,
  options?: Omit<
    RenderOptions<typeof import('@testing-library/dom/types/queries'), HTMLElement>,
    'queries'
  >,
) => render(ui, { wrapper: Wrapper, ...options });

const customRenderHook = <T extends unknown>(ui: (props: T) => T, options?: RenderHookOptions<T>) =>
  renderHook(ui, { wrapper: Wrapper, ...options });

export * from '@testing-library/react';
export { customRender, customRenderHook };
