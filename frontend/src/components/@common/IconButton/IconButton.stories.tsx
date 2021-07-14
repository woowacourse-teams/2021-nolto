import React from 'react';

import IconButton from './IconButton';
import Pencil from 'assets/pencil.svg';

export default {
  title: 'components/common/IconButton',
  component: IconButton,
  argTypes: {},
};

export const Default = () => (
  <IconButton>
    <Pencil />
  </IconButton>
);
