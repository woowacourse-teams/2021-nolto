import React, { useState } from 'react';

import DownPolygon from 'assets/downPolygon.svg';
import useDialog from 'contexts/dialog/useDialog';
import { PALETTE } from 'constants/palette';
import ROUTE from 'constants/routes';
import useMember from 'hooks/queries/useMember';
import Styled, { NotiLink, ProfileLink } from './UserProfile.styles';

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

  const notiCount = member.userData?.notifications;

  return (
    <Styled.Root className={className} onClick={() => setIsProfileOpen(!isProfileOpen)}>
      <Styled.UserThumbnail>
        <Styled.Image src={member.userData?.imageUrl} />
        <Styled.MoreProfileButton>
          <DownPolygon width="14px" fill={PALETTE.WHITE_400} />
        </Styled.MoreProfileButton>
        {notiCount > 0 && <Styled.NotiAlert>{notiCount}</Styled.NotiAlert>}
      </Styled.UserThumbnail>
      <Styled.Dropdown isOpen={isProfileOpen}>
        <Styled.Greeting>
          👋 Hello, {<br />} {member.userData?.nickname}!
        </Styled.Greeting>
        <NotiLink to={ROUTE.MYPAGE}>
          새 알림 {notiCount > 0 && <span className="noti-count">{notiCount}</span>}
        </NotiLink>
        <ProfileLink to={ROUTE.MYPAGE}>Profile</ProfileLink>
        <Styled.Button onClick={logout}>Logout</Styled.Button>
      </Styled.Dropdown>
    </Styled.Root>
  );
};

export default UserProfile;
