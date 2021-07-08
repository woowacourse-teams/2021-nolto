import React, { ButtonHTMLAttributes } from 'react';
import styled from 'styled-components';

import { ButtonStyle } from 'types';
import Styled from './Button.styles';

interface Props extends ButtonHTMLAttributes<HTMLButtonElement> {
  buttonStyle: ButtonStyle;
  reverse?: boolean;
  children: React.ReactNode;
}

const Button = ({ buttonStyle, reverse = false, children, className }: Props) => {
  return (
    <Styled.Root buttonStyle={buttonStyle} reverse={reverse} className={className}>
      {children}
    </Styled.Root>
  );
};

const Regular = styled(Button)<{ buttonStyle: ButtonStyle }>`
  border-radius: 8px;
`;

const Rounded = styled(Button)<{ buttonStyle: ButtonStyle }>`
  border-radius: 25px;
`;

export default { Regular, Rounded };
