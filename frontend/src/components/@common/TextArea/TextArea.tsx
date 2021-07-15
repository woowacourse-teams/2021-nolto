import React, { TextareaHTMLAttributes } from 'react';

import Styled from './TextArea.styles';

type Props = TextareaHTMLAttributes<HTMLTextAreaElement>;

const TextArea = React.forwardRef<HTMLTextAreaElement, Props>(({ ...options }, ref) => {
  return <Styled.Root ref={ref} {...options}></Styled.Root>;
});

export default TextArea;
