import React from 'react';

import pumpkinImg from 'assets/halloweenPumpkin.png';
import witchImg from 'assets/halloweenWitch.png';
import Styled from './CroppedEllipse.styles';

interface Props {
  toggleTheme: () => void;
}

const CroppedEllipse = ({ toggleTheme }: Props) => {
  return (
    <Styled.Root>
      <Styled.SvgRoot height="100%" width="100%">
        <ellipse cx="50%" cy="20px" rx="80%" ry="300px" fill="url(#grad1)" />
      </Styled.SvgRoot>
      <Styled.Pumpkin src={pumpkinImg} onClick={toggleTheme} alt="추석 복주머니" />
      <Styled.Witch src={witchImg} />
    </Styled.Root>
  );
};

export default CroppedEllipse;
