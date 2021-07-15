import React, { InputHTMLAttributes } from 'react';

import Styled from './Toggle.styles';

export interface Props extends InputHTMLAttributes<HTMLInputElement> {
  labelText?: string;
}

const Toggle = React.forwardRef<HTMLInputElement, Props>(({ labelText = '', ...options }, ref) => {
  return (
    <Styled.Label>
      <Styled.Text>{labelText}</Styled.Text>
      <Styled.Input type="checkbox" ref={ref} {...options} />
      <Styled.ToggleMark></Styled.ToggleMark>
    </Styled.Label>
  );
});

export default Toggle;
