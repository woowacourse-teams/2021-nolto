import React, { LabelHTMLAttributes } from 'react';
import Styled from './Label.styles';

interface Props extends LabelHTMLAttributes<HTMLLabelElement> {
  text: string;
}

const Label = ({ text }: Props) => {
  return <Styled.Root>{text}</Styled.Root>;
};

export default Label;
