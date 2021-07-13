import BaseLayout from 'components/BaseLayout/BaseLayout';
import React from 'react';

import Home from './Home';

export default {
  title: 'pages/Home',
  component: Home,
  argTypes: {},
};

export const Default = () => (
  <BaseLayout>
    <Home />
  </BaseLayout>
);
