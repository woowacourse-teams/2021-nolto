import { useContext } from 'react';

import { Context } from './SnackBarProvider';

const useSnackBar = () => {
  const context = useContext(Context);

  if (!context) {
    throw new Error('SnackBarProvider 내부에서만 useSnackBar hook을 사용할 수 있습니다.');
  }

  return context;
};

export default useSnackBar;
