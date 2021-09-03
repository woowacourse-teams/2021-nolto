import React from 'react';

import LargeSkeleton from './LargeSkeleton';

export default {
  title: 'components/LargeSkeleton',
  component: LargeSkeleton,
  argTypes: {},
};

export const Default = () => (
  <div style={{ width: '318px', height: '428px' }}>
    <LargeSkeleton />
  </div>
);
