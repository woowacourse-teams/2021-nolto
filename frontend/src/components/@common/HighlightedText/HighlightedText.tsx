import React, { HTMLAttributes } from 'react';

import { FONT_SIZE } from 'constants/styles';
import Styled from './HighlightedText.styles';

interface Props extends HTMLAttributes<HTMLSpanElement> {
  fontSize?: string;
  children: React.ReactNode;
}

const HighLightedText = ({ fontSize = FONT_SIZE.MEDIUM, children, ...option }: Props) => {
  return (
    <Styled.Root fontSize={fontSize} {...option}>
      {children}
    </Styled.Root>
  );
};

export default HighLightedText;
