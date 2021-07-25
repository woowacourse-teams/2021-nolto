import React, { useState } from 'react';

import Styled, { ToggleButton } from './Dropdown.styles';
import Dots from 'assets/dots.svg';

interface Props {
  children: React.ReactNode;
}

const Dropdown = ({ children }: Props) => {
  const [isTriggered, setIsTriggered] = useState(false);

  const toggle = () => {
    setIsTriggered(!isTriggered);
  };

  return (
    <Styled.Root>
      <ToggleButton onClick={toggle} isTriggered={isTriggered}>
        <Dots width="20px" />
      </ToggleButton>
      <Styled.Menu isTriggered={isTriggered}>{children}</Styled.Menu>
    </Styled.Root>
  );
};

export default Dropdown;
