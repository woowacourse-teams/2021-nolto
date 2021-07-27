import React from 'react';

import SnackBarProvider from 'context/snackBar/SnackBarProvider';
import HotFeedsContent from './HotFeedsContent';

export default {
  title: 'components/HotFeedsContent',
  component: HotFeedsContent,
  argTypes: {},
};

export const Default = () => (
  <SnackBarProvider>
    <HotFeedsContent />
  </SnackBarProvider>
);
