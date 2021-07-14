import React, { ButtonHTMLAttributes } from 'react';

import Styled from './IconButton.styles';

interface Props extends ButtonHTMLAttributes<HTMLButtonElement> {
  children: React.ReactNode;
}

const IconButton = ({ children, className, ...options }: Props) => {
  return (
    <Styled.Root className={className} {...options}>
      {children}
    </Styled.Root>
  );
};

export default IconButton;
