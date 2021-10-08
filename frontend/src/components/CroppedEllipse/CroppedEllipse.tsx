import React from 'react';

import luckyBag from 'assets/luckyBag.png';
import moonRabbit from 'assets/moonRabbit.gif';
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
      <Styled.LuckyBag src={luckyBag} width="24px" onClick={toggleTheme} alt="추석 복주머니" />
      <Styled.MoonRabbit src={moonRabbit} width="230px" />
    </Styled.Root>
  );
};

export default CroppedEllipse;
