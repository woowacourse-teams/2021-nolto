import React, { useState } from 'react';

import IconButton from '../IconButton/IconButton';
import Styled from './ToggleList.styles';

interface Props {
  width: string;
  height: string;
  children: React.ReactNode;
}

const ToggleList = ({ children, width, height }: Props) => {
  const [isToggled, setIsToggled] = useState(false);

  const handleClickToggle = () => {
    setIsToggled(!isToggled);
  };

  return (
    <Styled.Root width={width} height={height} $isToggled={isToggled}>
      <Styled.ContentWrapper $isToggled={isToggled}>
        <Styled.Content>{children}</Styled.Content>
      </Styled.ContentWrapper>
      <Styled.ButtonWrapper>
        <IconButton size="1.75rem" onClick={handleClickToggle} hasShadow={false}>
          <Styled.StacksMoreIcon $isToggled={isToggled} width={height} height={height} />
        </IconButton>
      </Styled.ButtonWrapper>
    </Styled.Root>
  );
};

export default ToggleList;
