import React, { InputHTMLAttributes } from 'react';

import Styled from './FormInput.styles';

type Props = InputHTMLAttributes<HTMLInputElement>;

const FormInput = ({ ...options }: Props) => {
  return <Styled.Root {...options} />;
};

export default FormInput;
