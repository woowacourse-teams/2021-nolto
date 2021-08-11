import { useContext } from 'react';

import { Context } from './SnackbarProvider';

const useSnackbar = () => {
  const context = useContext(Context);

  if (!context) {
    throw new Error('SnackbarProvider 내부에서만 useSnackbar hook을 사용할 수 있습니다.');
  }

  return context;
};

export default useSnackbar;
