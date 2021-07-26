import React from 'react';

import Styled from './Tooltip.styles';

interface Props {
  className?: string;
  children: React.ReactNode;
}

const Tooltip = ({ className, children }: Props) => {
  return <Styled.Root className={className}>{children}</Styled.Root>;
};

export default Tooltip;
