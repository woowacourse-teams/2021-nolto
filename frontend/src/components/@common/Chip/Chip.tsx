import React from 'react';
import styled from 'styled-components';

import { PALETTE } from 'constants/palette';
import Styled from './Chip.styles';

interface Props {
  className?: string;
  children: React.ReactNode;
}

const Chip = ({ className, children }: Props) => {
  return <Styled.Root className={className}>{children}</Styled.Root>;
};

const Dashed = styled(Chip)`
  border: 2px dashed ${PALETTE.PRIMARY_400};
  color: ${PALETTE.PRIMARY_400};
  line-height: 20px;
`;

const Solid = styled(Chip)`
  background: ${`linear-gradient(90deg, ${PALETTE.PRIMARY_200} 0%, ${PALETTE.PRIMARY_400} 100%)`};
  color: ${PALETTE.WHITE_400};
  line-height: 24px;
`;

export default { Dashed, Solid };
