import { useContext } from 'react';

import { Context } from 'storage/user/UserInfoProvider';

const useUserInfo = () => {
  const context = useContext(Context);

  if (!context) {
    throw new Error('UserInfoProvider 내부에서만 useUserInfo hook을 사용할 수 있습니다.');
  }

  return context;
};

export default useUserInfo;
