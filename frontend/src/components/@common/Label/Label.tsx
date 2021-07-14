import React, { LabelHTMLAttributes } from 'react';
import Styled from './Label.styles';

interface Props extends LabelHTMLAttributes<HTMLLabelElement> {
  text: string;
  required?: boolean;
}

const Label = ({ text, required = false, className }: Props) => {
  return (
    <Styled.Root className={className} required={required}>
      {text}
    </Styled.Root>
  );
};

export default Label;
