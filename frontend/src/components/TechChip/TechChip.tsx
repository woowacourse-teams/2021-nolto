import React from 'react';

import { FeedStatus } from 'types';
import { STEP_CONVERTER } from 'constants/common';
import Styled from './TechChip.styles';

interface Props {
  step: FeedStatus;
}

const TechChip = ({ step }: Props) => {
  return <Styled.Root step={step}>{STEP_CONVERTER[step]}</Styled.Root>;
};

export default TechChip;
