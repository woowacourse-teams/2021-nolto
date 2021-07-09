import React from 'react';

import Styled from './HighlightedText.styles';

interface Props {
  fontSize: string;
  children: React.ReactNode;
}

const HighLightedText = ({ fontSize, children }: Props) => {
  return <Styled.Root fontSize={fontSize}>{children}</Styled.Root>;
};

export default HighLightedText;
