import React from 'react';

import Styled from './CroppedEllipse.styles';

const CroppedEllipse = () => {
  return (
    <Styled.Root height="480px" width="100vw">
      <defs>
        <linearGradient id="grad1" x1="0%" y1="0%" x2="100%" y2="0%">
          <stop offset="0%" stopColor="#F6D365" stopOpacity="1" />
          <stop offset="100%" stopColor="#FDA085" stopOpacity="1" />
        </linearGradient>
      </defs>
      <ellipse cx="50%" cy="100" rx="80%" ry="75%" fill="url(#grad1)" />
    </Styled.Root>
  );
};

export default CroppedEllipse;
