import React, { ButtonHTMLAttributes } from 'react';

import Styled from './IconButton.styles';

interface Props extends ButtonHTMLAttributes<HTMLButtonElement> {
  children: React.ReactNode;
  isShadow?: boolean;
  size: string;
  hasHoverAnimation?: boolean;
}

const IconButton = ({ children, isShadow = true, hasHoverAnimation = true, ...options }: Props) => {
  return (
    <Styled.Root isShadow={isShadow} hasHoverAnimation={hasHoverAnimation} {...options}>
      <Styled.Icon>{children}</Styled.Icon>
    </Styled.Root>
  );
};

export default IconButton;
