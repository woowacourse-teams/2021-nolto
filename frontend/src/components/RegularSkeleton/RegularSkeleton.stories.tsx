import React from 'react';

import RegularSkeleton from './RegularSkeleton';

export default {
  title: 'components/RegularSkeleton',
  component: RegularSkeleton,
  argTypes: {},
};

export const Default = () => (
  <div style={{ width: '236px', height: '364px' }}>
    <RegularSkeleton />
  </div>
);
