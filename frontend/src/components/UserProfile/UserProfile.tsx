import React, { useState } from 'react';

import DownPolygon from 'assets/downPolygon.svg';
import useDialog from 'context/dialog/useDialog';
import { PALETTE } from 'constants/palette';
import ROUTE from 'constants/routes';
import useMember from 'hooks/queries/useMember';
import Styled, { Link } from './UserProfile.styles';

interface Props {
  className?: string;
}

const UserProfile = ({ className }: Props) => {
  const [isProfileOpen, setIsProfileOpen] = useState(false);

  const member = useMember();
  const dialog = useDialog();

  const logout = () => {
    member.logout();
    dialog.alert('로그아웃되었습니다.');
  };

  return (
    <Styled.Root className={className} onClick={() => setIsProfileOpen(!isProfileOpen)}>
      <Styled.UserThumbnail>
        <Styled.Image src={member.userData?.imageUrl} />
        <Styled.MoreProfileButton>
          <DownPolygon width="14px" fill={PALETTE.WHITE_400} />
        </Styled.MoreProfileButton>
      </Styled.UserThumbnail>
      <Styled.Dropdown isOpen={isProfileOpen}>
        <Styled.Greeting>👋 Hello, {member.userData?.nickname}!</Styled.Greeting>
        <Link to={ROUTE.MYPAGE}>Profile</Link>
        <Styled.Button onClick={logout}>Logout</Styled.Button>
      </Styled.Dropdown>
    </Styled.Root>
  );
};

export default UserProfile;
