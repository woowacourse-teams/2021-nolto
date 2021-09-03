import React from 'react';

import { ERROR_MSG } from 'constants/message';
import ErrorFallback from './ErrorFallback';

export default {
  title: 'components/ErrorFallback',
  component: ErrorFallback,
  argTypes: {},
};

export const Default = () => <ErrorFallback message={ERROR_MSG.LOAD_DATA} />;
