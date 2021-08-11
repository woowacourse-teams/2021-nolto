import React, { HTMLAttributes } from 'react';

import Styled from './HighlightedText.styles';

interface Props extends HTMLAttributes<HTMLSpanElement> {
  fontSize?: string;
  children: React.ReactNode;
}

const HighLightedText = ({ fontSize = '1rem', children, ...option }: Props) => {
  return (
    <Styled.Root fontSize={fontSize} {...option}>
      {children}
    </Styled.Root>
  );
};

export default HighLightedText;
