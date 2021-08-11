import React, { InputHTMLAttributes } from 'react';

import Styled from './TextInput.styles';

type Props = InputHTMLAttributes<HTMLInputElement>;

const TextInput = React.forwardRef<HTMLInputElement, Props>(({ ...options }, ref) => {
  return <Styled.Root ref={ref} {...options} />;
});

export default TextInput;
