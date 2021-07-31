import React from 'react';

import ErrorFallback from './ErrorFallback';

export default {
  title: 'components/ErrorFallback',
  component: ErrorFallback,
  argTypes: {},
};

export const Default = () => <ErrorFallback message="데이터를 불러올 수 없습니다." />;
