import { useContext } from 'react';

import { Context } from './NotificationProvider';

const useNotification = () => {
  const context = useContext(Context);

  if (!context) {
    throw new Error('NotificationProvider 내부에서만 useModal hook을 사용할 수 있습니다.');
  }

  return context;
};

export default useNotification;
