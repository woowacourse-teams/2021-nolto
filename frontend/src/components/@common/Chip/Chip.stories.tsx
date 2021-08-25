import React from 'react';

import { FlexContainer } from 'commonStyles';
import Chip from './Chip';

export default {
  title: 'components/common/Chip',
  component: Chip,
};

export const Progress = () => (
  <FlexContainer>
    <Chip.Dashed>조립중</Chip.Dashed>
  </FlexContainer>
);

export const Complete = () => (
  <FlexContainer>
    <Chip.Solid>전시중</Chip.Solid>
  </FlexContainer>
);
