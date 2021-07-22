import React, { useEffect, useState } from 'react';

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

  useEffect(() => {
    const accessToken = localStorage.getItem('accessToken');

    if (accessToken) {
      setUserInfo({
        email: 'dummy1@email.com',
      });
    }
  }, []);

  return (
    <Context.Provider value={{ userInfo, setUserInfo, removeUserInfo }}>
      {children}
    </Context.Provider>
  );
};

export default UserInfoProvider;
