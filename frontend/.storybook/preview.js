import { configure, addDecorator } from '@storybook/react';
import { MemoryRouter } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from 'react-query';

import GlobalStyle from '../src/Global.styles';
import AsyncBoundary from '../src/components/AsyncBoundary';
import ModalProvider from '../src/contexts/modal/ModalProvider';
import DialogProvider from '../src/contexts/dialog/DialogProvider';
import SnackbarProvider from '../src/contexts/snackbar/SnackbarProvider';
import { CommentModuleContext } from '../src/components/CommentModule/CommentModule';

export const parameters = {
  actions: { argTypesRegex: '^on[A-Z].*' },
  controls: {
    matchers: {
      color: /(background|color)$/i,
      date: /Date$/,
    },
  },
};

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      suspense: true,
      useErrorBoundary: true,
    },
  },
});

addDecorator((story) => (
  <>
    <QueryClientProvider client={queryClient}>
      <MemoryRouter initialEntries={['/']}>
        <GlobalStyle />
        <DialogProvider>
          <SnackbarProvider>
            <AsyncBoundary rejectedFallback={<div>에러났어용 🚨</div>}>
              <ModalProvider>
                <CommentModuleContext.Provider value={{ feedId: 1, addCommentCount: () => {} }}>
                  {story()}
                </CommentModuleContext.Provider>
              </ModalProvider>
            </AsyncBoundary>
          </SnackbarProvider>
        </DialogProvider>
      </MemoryRouter>
    </QueryClientProvider>
  </>
));

configure(require.context('../src', true, /\.stories\.js?$/), module);

localStorage.setItem(
  'accessToken',
  'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI2IiwiaWF0IjoxNjI4MDU2NjMzLCJleHAiOjE2MjgwNjAyMzN9.ic01hzhIonPcW1u50Do6u05sxbLp4H09-UwXRUultew',
);

if (typeof global.process === 'undefined') {
  const { worker } = require('../src/__mocks__/msw/browser');

  worker.start();
}
