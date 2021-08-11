import React, { ButtonHTMLAttributes } from 'react';

import Styled from './IconButton.styles';

interface Props extends ButtonHTMLAttributes<HTMLButtonElement> {
  children: React.ReactNode;
  isShadow?: boolean;
}

const IconButton = ({ children, isShadow = true, ...options }: Props) => {
  return (
    <Styled.Root isShadow={isShadow} {...options}>
      {children}
    </Styled.Root>
  );
};

export default IconButton;
