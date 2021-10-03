import { useContext } from 'react';

import { Context } from './MemberProvider';

const useMember = () => {
  const context = useContext(Context);

  if (!context) {
    throw new Error('MemberProvider 내부에서만 useMember hook을 사용할 수 있습니다.');
  }

  return context;
};

export default useMember;
