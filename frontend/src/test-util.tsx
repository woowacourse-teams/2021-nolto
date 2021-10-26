import React from 'react';
import { QueryClient, QueryClientProvider, setLogger } from 'react-query';
import { MemoryRouter } from 'react-router-dom';

import { render, RenderOptions } from '@testing-library/react';
import { renderHook } from '@testing-library/react-hooks';
import { RenderHookOptions } from '@testing-library/react-hooks/lib/types';

import DialogProvider from 'contexts/dialog/DialogProvider';
import ModalProvider from 'contexts/modal/ModalProvider';
import SnackbarProvider from 'contexts/snackbar/SnackbarProvider';
import MemberProvider from 'contexts/member/MemberProvider';
import AsyncBoundary from 'components/AsyncBoundary';
import ErrorFallback from 'components/ErrorFallback/ErrorFallback';

interface WrapperProps {
  children?: React.ReactNode;
}

setLogger({
  log: console.log,
  warn: console.warn,
  // eslint-disable-next-line @typescript-eslint/no-empty-function
  error: () => {},
});

const Wrapper = ({ children }: WrapperProps) => {
  const queryClient = new QueryClient({
    defaultOptions: {
      queries: {
        retry: false,
      },
    },
  });

  return (
    <QueryClientProvider client={queryClient}>
      <SnackbarProvider>
        <DialogProvider>
          <ModalProvider>
            <MemberProvider>
              <MemoryRouter initialEntries={['/']}>
                <AsyncBoundary rejectedFallback={<ErrorFallback message="테스트 에러" />}>
                  {children}
                </AsyncBoundary>
              </MemoryRouter>
            </MemberProvider>
          </ModalProvider>
        </DialogProvider>
      </SnackbarProvider>
    </QueryClientProvider>
  );
};

const customRender = <T extends unknown>(
  ui: React.ReactElement<T, string | React.JSXElementConstructor<T>>,
  options?: Omit<
    RenderOptions<typeof import('@testing-library/dom/types/queries'), HTMLElement>,
    'queries'
  >,
) => render(ui, { wrapper: Wrapper, hydrate: true, ...options });

const customRenderHook = <T extends unknown>(ui: (props: T) => T, options?: RenderHookOptions<T>) =>
  renderHook(ui, { wrapper: Wrapper, ...options });

export * from '@testing-library/react';
export { customRender, customRenderHook };
