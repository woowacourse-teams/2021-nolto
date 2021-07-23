import { useContext } from 'react';

import { Context } from './TechTagProvider';

const useTechTag = () => {
  const context = useContext(Context);

  if (!context) {
    throw new Error('TechTagProvider 내부에서만 useTechTag hook을 사용할 수 있습니다.');
  }

  return context;
};

export default useTechTag;
