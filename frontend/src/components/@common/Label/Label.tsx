import React, { LabelHTMLAttributes } from 'react';
import Styled from './Label.styles';

interface Props extends LabelHTMLAttributes<HTMLLabelElement> {
  text: string;
  required?: boolean;
}

const Label = ({ text, required = false, className, ...options }: Props) => {
  return (
    <Styled.Root className={className} required={required} {...options}>
      {text}
    </Styled.Root>
  );
};

export default Label;
