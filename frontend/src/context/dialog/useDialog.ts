import { useContext } from 'react';

import { Context } from './DialogProvider';

const useDialog = () => {
  const context = useContext(Context);

  if (!context) {
    throw new Error('DialogProvider 내부에서만 useModal hook을 사용할 수 있습니다.');
  }

  return context;
};

export default useDialog;
