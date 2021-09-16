import React from 'react';

import { FeedStep } from 'types';
import { STEP_CONVERTER } from 'constants/common';
import Styled from './StepChip.styles';

interface Props {
  step: FeedStep;
  selected?: boolean;
}

const StepChip = ({ step, selected }: Props) => {
  return (
    <Styled.Root step={step} selected={selected}>
      {STEP_CONVERTER[step]}
    </Styled.Root>
  );
};

export default StepChip;
