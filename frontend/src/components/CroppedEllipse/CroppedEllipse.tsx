import React from 'react';

import { PALETTE } from 'constants/palette';
import Styled from './CroppedEllipse.styles';

const CroppedEllipse = () => {
  return (
    <Styled.Root>
      <Styled.SvgRoot height="400px" width="100%">
        <defs>
          <linearGradient id="grad1" x1="0%" y1="0%" x2="100%" y2="0%">
            <stop offset="0%" stopColor={PALETTE.PRIMARY_200} stopOpacity="1" />
            <stop offset="100%" stopColor={PALETTE.PRIMARY_400} stopOpacity="1" />
          </linearGradient>
        </defs>
        <ellipse cx="50%" cy="20px" rx="80%" ry="360px" fill="url(#grad1)" />
      </Styled.SvgRoot>
      <Styled.Horse width="84" />
    </Styled.Root>
  );
};

export default CroppedEllipse;
