import { useContext } from 'react';

import { Context } from './RepliesProvider';

const useRepliesProvider = () => {
  const context = useContext(Context);

  if (!context) {
    throw new Error('RepliesProvider 내부에서만 useRepliesProvider hook을 사용할 수 있습니다.');
  }

  return context;
};

export default useRepliesProvider;
