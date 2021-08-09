import React, { useEffect } from 'react';

import SnackBarProvider from './SnackBarProvider';
import useSnackBar from './useSnackBar';

export default {
  title: 'context/SnackBarProvider',
  component: SnackBarProvider,
  argTypes: {},
};

const Page = () => {
  const snackBar = useSnackBar();

  useEffect(() => {
    snackBar.addSnackBar('error', '에러났어용~');
    snackBar.addSnackBar('success', '성공했어요~');
  }, []);

  return (
    <div>
      <button onClick={() => snackBar.addSnackBar('error', '에러났어용~')}>성공 스낵바</button>
      <button onClick={() => snackBar.addSnackBar('success', '성공했어요~')}>실패 스낵바</button>
    </div>
  );
};

export const Default = () => <Page />;
