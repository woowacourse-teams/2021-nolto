import React from 'react';

import { FeedStatus } from 'types';
import { STEP_CONVERTER } from 'constants/common';
import Styled from './StepChip.styles';

interface Props {
  step: FeedStatus;
  onSelect?: () => void;
  selected?: boolean;
}

const StepChip = ({ step, onSelect, selected }: Props) => {
  return onSelect ? (
    <Styled.Button type="button" onClick={onSelect}>
      <Styled.ClickableRoot step={step} selected={selected}>
        {STEP_CONVERTER[step]}
      </Styled.ClickableRoot>
    </Styled.Button>
  ) : (
    <Styled.DefaultRoot step={step}>{STEP_CONVERTER[step]}</Styled.DefaultRoot>
  );
};

export default StepChip;
