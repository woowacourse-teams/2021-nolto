import React from 'react';

import Styled from './LargeSkeleton.styles';

const LargeSkeleton = () => {
  return (
    <Styled.Root>
      <Styled.AvatarContainer>
        <Styled.UserImage />
        <Styled.Nickname />
      </Styled.AvatarContainer>
      <Styled.FeedContainer>
        <Styled.Image />
        <Styled.ContentWrapper>
          <Styled.Title />
          <Styled.Content />
          <Styled.Content />
        </Styled.ContentWrapper>
      </Styled.FeedContainer>
    </Styled.Root>
  );
};

export default LargeSkeleton;
