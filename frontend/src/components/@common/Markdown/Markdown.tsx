import React from 'react';

import Styled from './Markdown.styles';

interface Props {
  children: string;
}

const Markdown = ({ children }: Props) => {
  return (
    <Styled.Root
      components={{
        a({ node, children, ...props }) {
          return (
            <a target="_blank" {...props}>
              {children}
            </a>
          );
        },
      }}
    >
      {children}
    </Styled.Root>
  );
};

export default Markdown;
