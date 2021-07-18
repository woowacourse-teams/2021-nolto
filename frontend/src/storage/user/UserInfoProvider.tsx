import React, { useState } from 'react';

import { UserInfo } from 'types';

interface Props {
  children: React.ReactNode;
}

interface UserInfoContext {
  userInfo: UserInfo;
  setUserInfo: React.Dispatch<React.SetStateAction<UserInfo>>;
  removeUserInfo: () => void;
}

export const Context = React.createContext<UserInfoContext | null>(null);

const UserInfoProvider = ({ children }: Props) => {
  const [userInfo, setUserInfo] = useState<UserInfo | null>(null);

  const removeUserInfo = () => {
    setUserInfo(null);
  };

  return (
    <Context.Provider value={{ userInfo, setUserInfo, removeUserInfo }}>
      {children}
    </Context.Provider>
  );
};

export default UserInfoProvider;
