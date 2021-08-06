import { useContext } from 'react';

import { Context } from './CommentsProviderModule';

const useCommentsModule = () => {
  const context = useContext(Context);

  if (!context) {
    throw new Error(
      'CommentsProviderModule 내부에서만 useCommentsModule hook을 사용할 수 있습니다.',
    );
  }

  return context;
};

export default useCommentsModule;
