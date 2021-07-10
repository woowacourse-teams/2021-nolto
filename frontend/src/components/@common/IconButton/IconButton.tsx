import React, { ButtonHTMLAttributes } from 'react';

import Styled from './IconButton.styles';

interface Props extends ButtonHTMLAttributes<HTMLButtonElement> {
  children: React.ReactNode;
}

const IconButton = ({ children }: Props) => {
  return <Styled.Root>{children}</Styled.Root>;
};

export default IconButton;
