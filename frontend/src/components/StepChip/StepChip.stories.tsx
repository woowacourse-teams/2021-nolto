import React from 'react';
import { FeedStep } from 'types';

import StepChip from './StepChip';

export default {
  title: 'components/StepChip',
  component: StepChip,
  argTypes: {},
};

export const PROGRESS = () => <StepChip step={FeedStep.PROGRESS} />;
export const COMPLETE = () => <StepChip step={FeedStep.COMPLETE} />;
