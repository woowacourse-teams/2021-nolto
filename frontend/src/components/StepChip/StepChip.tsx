import React from 'react';

import { FeedStep } from 'types';
import { STEP_CONVERTER } from 'constants/common';
import Styled from './StepChip.styles';

interface Props {
  step: FeedStep;
  selected?: boolean;
  className?: string;
}

const StepChip = ({ step, selected, className }: Props) => {
  return (
    <Styled.Root className={className} step={step} selected={selected}>
      {STEP_CONVERTER[step]}
    </Styled.Root>
  );
};

export default StepChip;
