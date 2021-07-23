import React from 'react';
import { FeedStatus } from 'types';

import StepChip from './StepChip';

export default {
  title: 'components/StepChip',
  component: StepChip,
  argTypes: {},
};

export const PROGRESS = () => <StepChip step={FeedStatus.PROGRESS} />;
export const COMPLETE = () => <StepChip step={FeedStatus.COMPLETE} />;
