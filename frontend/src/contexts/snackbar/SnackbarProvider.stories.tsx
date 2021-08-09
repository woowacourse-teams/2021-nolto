import React, { useEffect } from 'react';

import SnackbarProvider from './SnackbarProvider';
import useSnackbar from './useSnackbar';

export default {
  title: 'contexts/SnackbarProvider',
  component: SnackbarProvider,
  argTypes: {},
};

const Page = () => {
  const snackbar = useSnackbar();

  useEffect(() => {
    snackbar.addSnackbar('error', '에러났어용~');
    snackbar.addSnackbar('success', '성공했어요~');
  }, []);

  return (
    <div>
      <button onClick={() => snackbar.addSnackbar('error', '에러났어용~')}>성공 스낵바</button>
      <button onClick={() => snackbar.addSnackbar('success', '성공했어요~')}>실패 스낵바</button>
    </div>
  );
};

export const Default = () => <Page />;
