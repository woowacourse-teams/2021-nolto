import React from 'react';

import Styled from './HighlightedText.styles';

interface Props {
  fontSize: string;
  children: React.ReactNode;
  className?: string;
}

const HighLightedText = ({ fontSize, children, className }: Props) => {
  return (
    <Styled.Root className={className} fontSize={fontSize}>
      {children}
    </Styled.Root>
  );
};

export default HighLightedText;
