import React from 'react';

import { FeedStatus } from 'types';
import { STEP_CONVERTER } from 'constants/common';
import Styled from './StepChip.styles';

interface Props {
  step: FeedStatus;
}

const StepChip = ({ step }: Props) => {
  return <Styled.Root step={step}>{STEP_CONVERTER[step]}</Styled.Root>;
};

export default StepChip;
