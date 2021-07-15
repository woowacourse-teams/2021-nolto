import React, { InputHTMLAttributes } from 'react';

import Styled from './RadioButton.styles';

export interface Props extends InputHTMLAttributes<HTMLInputElement> {
  labelText?: string;
}

const RadioButton = React.forwardRef<HTMLInputElement, Props>(
  ({ labelText = '', name, ...options }, ref) => {
    return (
      <Styled.Label>
        <Styled.Text>{labelText}</Styled.Text>
        <Styled.RadioButton ref={ref} type="radio" name={name} {...options} />
        <Styled.RadioMark />
      </Styled.Label>
    );
  },
);

export default RadioButton;
