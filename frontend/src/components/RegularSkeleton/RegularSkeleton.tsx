import React from 'react';

import Styled from './RegularSkeleton.styles';

const RegularSkeleton = () => {
  return (
    <Styled.Root>
      <Styled.AvatarContainer>
        <Styled.UserImage />
        <Styled.Nickname />
      </Styled.AvatarContainer>
      <Styled.RegularCardImgWrapper>
        <Styled.Image />
      </Styled.RegularCardImgWrapper>
      <Styled.ContentWrapper>
        <Styled.Title />
        <Styled.Content />
        <Styled.Content />
      </Styled.ContentWrapper>
    </Styled.Root>
  );
};

export default RegularSkeleton;
