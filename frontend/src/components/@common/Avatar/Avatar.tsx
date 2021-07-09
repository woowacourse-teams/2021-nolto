import React from 'react';

import { User } from 'types';
import Styled from './Avatar.styles';

interface Props {
  user: User;
}

const Avatar = ({ user }: Props) => {
  return (
    <Styled.Root>
      <Styled.Image src={user.imageUrl} alt={`닉네임 ${user.nickname}`} />
      <Styled.Nickname>{user.nickname}</Styled.Nickname>
    </Styled.Root>
  );
};

export default Avatar;
