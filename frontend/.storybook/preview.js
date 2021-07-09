import { configure, addDecorator } from '@storybook/react';

import GlobalStyle from '../src/Global.styles';

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
    <GlobalStyle />
    {story()}
  </>
));

configure(require.context('../src', true, /\.stories\.js?$/), module);
