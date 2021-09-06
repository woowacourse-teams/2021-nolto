import React from 'react';

import IconButton from './IconButton';
import DownPolygon from 'assets/downPolygon.svg';

export default {
  title: 'components/common/IconButton',
  component: IconButton,
  argTypes: {},
};

export const Default = () => (
  <IconButton size="5rem">
    <DownPolygon fill="black" />
  </IconButton>
);
