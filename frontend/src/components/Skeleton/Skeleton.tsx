import React from 'react';

import Styled from './Skeleton.styles';

const Skeleton = () => {
  return (
    <div>
      <Styled.AvatarContainer>
        <Styled.Image />
        <Styled.Nickname />
      </Styled.AvatarContainer>
      <Styled.CardContainer>
        <Styled.Thumbnail />
        <Styled.ContentArea>
          <Styled.Title />
          <Styled.Content />
          <Styled.Content />
        </Styled.ContentArea>
      </Styled.CardContainer>
    </div>
  );
};

export default Skeleton;
