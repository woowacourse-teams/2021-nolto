import React, { InputHTMLAttributes } from 'react';

import Styled from './Toggle.styles';

export interface Props extends InputHTMLAttributes<HTMLInputElement> {
  labelText?: string;
  fontSize?: string;
}

const Toggle = React.forwardRef<HTMLInputElement, Props>(
  ({ labelText = '', fontSize, ...options }, ref) => {
    return (
      <Styled.Label>
        <Styled.Text fontSize={fontSize}>{labelText}</Styled.Text>
        <Styled.Input type="checkbox" ref={ref} {...options} />
        <Styled.ToggleMark></Styled.ToggleMark>
      </Styled.Label>
    );
  },
);

export default Toggle;
