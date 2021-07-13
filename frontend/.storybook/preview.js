import { configure, addDecorator } from '@storybook/react';
import { MemoryRouter } from 'react-router-dom';

import GlobalStyle from '../src/Global.styles';
import BaseLayout from 'components/BaseLayout/BaseLayout';

export const parameters = {
  actions: { argTypesRegex: '^on[A-Z].*' },
  controls: {
    matchers: {
      color: /(background|color)$/i,
      date: /Date$/,
    },
  },
};

addDecorator((story) => (
  <>
    <MemoryRouter initialEntries={['/']}>
      <GlobalStyle />
      <BaseLayout>{story()}</BaseLayout>
    </MemoryRouter>
  </>
));

configure(require.context('../src', true, /\.stories\.js?$/), module);
