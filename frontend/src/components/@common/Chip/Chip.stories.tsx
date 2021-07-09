import React from 'react';

import Chip from './Chip';

export default {
  title: 'components/common/Chip',
  component: Chip,
  argTypes: {},
};

export const Progress = () => <Chip.Dashed>조립중</Chip.Dashed>;

export const Complete = () => <Chip.Solid>전시중</Chip.Solid>;
