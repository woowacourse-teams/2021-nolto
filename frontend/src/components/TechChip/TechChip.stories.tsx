import React from 'react';
import { FeedStatus } from 'types';

import TechChip from './TechChip';

export default {
  title: 'components/TechChip',
  component: TechChip,
  argTypes: {},
};

export const PROGRESS = () => <TechChip step={FeedStatus.PROGRESS} />;
export const COMPLETE = () => <TechChip step={FeedStatus.COMPLETE} />;
