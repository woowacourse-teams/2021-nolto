import React, { ButtonHTMLAttributes } from 'react';
import styled from 'styled-components';

import { ButtonStyle } from 'types';
import Styled from './TextButton.styles';

interface Props extends ButtonHTMLAttributes<HTMLButtonElement> {
  buttonStyle: ButtonStyle;
  reverse?: boolean;
  children: React.ReactNode;
}

const TextButton = ({ buttonStyle, reverse = false, children, className, ...options }: Props) => {
  return (
    <Styled.Root $buttonStyle={buttonStyle} $reverse={reverse} className={className} {...options}>
      {children}
    </Styled.Root>
  );
};

const Regular = styled(TextButton)`
  border-radius: 8px;
`;

const Rounded = styled(TextButton)`
  border-radius: 25px;
`;

export default { Regular, Rounded };
