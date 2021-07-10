import React, { TextareaHTMLAttributes } from 'react';

import Styled from './TextArea.styles';

type Props = TextareaHTMLAttributes<HTMLTextAreaElement>;

const TextArea = ({ ...options }: Props) => {
  return <Styled.Root {...options}></Styled.Root>;
};

export default TextArea;
