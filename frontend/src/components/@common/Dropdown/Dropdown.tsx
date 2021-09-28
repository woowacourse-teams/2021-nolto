import React, { useState } from 'react';

import Dots from 'assets/dots.svg';
import Styled, { ToggleButton } from './Dropdown.styles';
import useFocusOut from 'hooks/@common/useFocusOut';

interface Props {
  children: React.ReactNode;
}

const Dropdown = ({ children }: Props) => {
  const [isTriggered, setIsTriggered] = useState(false);
  const ref = useFocusOut(() => {
    setIsTriggered(false);
  });

  const toggle = () => {
    setIsTriggered(!isTriggered);
  };

  return (
    <Styled.Root ref={ref}>
      <ToggleButton onClick={toggle} isTriggered={isTriggered}>
        <Dots width="20px" />
      </ToggleButton>
      <Styled.Menu isTriggered={isTriggered}>{children}</Styled.Menu>
    </Styled.Root>
  );
};

export default Dropdown;
