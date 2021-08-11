import React from 'react';

import { Author } from 'types';
import Styled from './Avatar.styles';

interface Props {
  user: Author;
  className?: string;
}

const Avatar = ({ user, className }: Props) => {
  if (!user) {
    return null;
  }

  return (
    <Styled.Root className={className}>
      <Styled.Image src={user.imageUrl} alt={`닉네임 ${user.nickname}`} />
      <Styled.Nickname>{user.nickname}</Styled.Nickname>
    </Styled.Root>
  );
};

export default Avatar;
