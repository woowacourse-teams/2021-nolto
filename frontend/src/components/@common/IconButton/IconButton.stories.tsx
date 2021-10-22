import React from 'react';

import IconButton from './IconButton';
import Mail from 'assets/mail.svg';

export default {
  title: 'components/common/IconButton',
  component: IconButton,
  argTypes: {},
};

export const Default = () => (
  <IconButton size="5rem">
    <Mail fill="black" />
  </IconButton>
);
