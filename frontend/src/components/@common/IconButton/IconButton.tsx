import React, { ButtonHTMLAttributes } from 'react';

import Styled from './IconButton.styles';

interface Props extends ButtonHTMLAttributes<HTMLButtonElement> {
  children: React.ReactNode;
  hasShadow?: boolean;
  size: string;
  hasHoverAnimation?: boolean;
  className?: string;
}

const IconButton = ({
  children,
  hasShadow = true,
  hasHoverAnimation = true,
  className,
  ...options
}: Props) => {
  return (
    <Styled.Root
      hasShadow={hasShadow}
      hasHoverAnimation={hasHoverAnimation}
      className={className}
      {...options}
    >
      <Styled.Icon>{children}</Styled.Icon>
    </Styled.Root>
  );
};

export default IconButton;
