import { PALETTE } from 'constants/palette';
import React from 'react';

import Styled from './SOSFlag.styles';

interface Props {
  className?: string;
}

const SOSFlag = ({ className }: Props) => {
  return (
    <Styled.Root className={className}>
      <Styled.FrontSide>도움 요청</Styled.FrontSide>
      <svg height="6" width="8">
        <polygon points="0,0 8,0 8,6" fill={PALETTE.PRIMARY_SHADOW} />
      </svg>
    </Styled.Root>
  );
};

export default SOSFlag;
