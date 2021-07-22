import { configure, addDecorator } from '@storybook/react';
import { MemoryRouter } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from 'react-query';

import GlobalStyle from '../src/Global.styles';
import ModalProvider from '../src/context/modal/ModalProvider';

export const parameters = {
  actions: { argTypesRegex: '^on[A-Z].*' },
  controls: {
    matchers: {
      color: /(background|color)$/i,
      date: /Date$/,
    },
  },
};

const queryClient = new QueryClient();

addDecorator((story) => (
  <>
    <QueryClientProvider client={queryClient}>
      <MemoryRouter initialEntries={['/']}>
        <GlobalStyle />
        <ModalProvider>{story()}</ModalProvider>
      </MemoryRouter>
    </QueryClientProvider>
  </>
));

configure(require.context('../src', true, /\.stories\.js?$/), module);
