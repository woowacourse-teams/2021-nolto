import React from 'react';
import styled from 'styled-components';

import Styled from './Card.styles';

interface Props {
  children: React.ReactNode;
  className?: string;
}

const Card = ({ children, className }: Props) => {
  return <Styled.Root className={className}>{children}</Styled.Root>;
};

const Regular = styled(Card)`
  width: 280px;
  height: 352px;
`;

const Stretch = styled(Card)`
  width: 754px;
  height: 136px;
`;

export default { Regular, Stretch };
