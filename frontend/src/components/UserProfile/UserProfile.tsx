import React, { useState } from 'react';

import DownPolygon from 'assets/downPolygon.svg';
import useNotification from 'context/notification/useNotification';
import { PALETTE } from 'constants/palette';
import Styled from './UserProfile.styles';

interface Props {
  className?: string;
}

const UserProfile = ({ className }: Props) => {
  const [isProfileOpen, setIsProfileOpen] = useState(false);
  const notification = useNotification();

  const goUserProfile = () => {
    notification.alert('프로필 기능 구현 중...');
  };

  const logout = () => {
    notification.alert('로그아웃되었습니다.');
  };

  return (
    <Styled.Root className={className} onClick={() => setIsProfileOpen(!isProfileOpen)}>
      <Styled.UserThumbnail>
        <Styled.Image src="https://avatars.githubusercontent.com/u/48755175?v=4" />
        <Styled.MoreProfileButton>
          <DownPolygon width="14px" fill={PALETTE.WHITE_400} />
        </Styled.MoreProfileButton>
      </Styled.UserThumbnail>
      <Styled.Dropdown isOpen={isProfileOpen}>
        <Styled.Greeting>👋 Hello, 미키!</Styled.Greeting>
        <Styled.Button onClick={goUserProfile}>Profile</Styled.Button>
        <Styled.Button onClick={logout}>Logout</Styled.Button>
      </Styled.Dropdown>
    </Styled.Root>
  );
};

export default UserProfile;
