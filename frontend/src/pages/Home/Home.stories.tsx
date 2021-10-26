import React from 'react';

import Home from './Home';

export default {
  title: 'pages/Home',
  component: Home,
  argTypes: {},
};

export const Default = () => <Home toggleTheme={() => {}} />;
