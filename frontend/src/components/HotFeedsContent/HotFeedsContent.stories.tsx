import React from 'react';

import SnackbarProvider from 'context/snackbar/SnackbarProvider';
import HotFeedsContent from './HotFeedsContent';

export default {
  title: 'components/HotFeedsContent',
  component: HotFeedsContent,
  argTypes: {},
};

export const Default = () => (
  <SnackbarProvider>
    <HotFeedsContent />
  </SnackbarProvider>
);
