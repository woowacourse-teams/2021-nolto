import React, { InputHTMLAttributes } from 'react';

import Styled from './FormInput.styles';

type Props = InputHTMLAttributes<HTMLInputElement>;

const FormInput = React.forwardRef<HTMLInputElement, Props>(({ ...options }, ref) => {
  return <Styled.Root ref={ref} {...options} />;
});

export default FormInput;
