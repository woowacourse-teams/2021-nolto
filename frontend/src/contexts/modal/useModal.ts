import { useContext } from 'react';

import { Context } from './ModalProvider';

const useModal = () => {
  const context = useContext(Context);

  if (!context) {
    throw new Error('ModalProvider 내부에서만 useModal hook을 사용할 수 있습니다.');
  }

  return context;
};

export default useModal;
