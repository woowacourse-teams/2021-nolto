import React from 'react';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { vs } from 'react-syntax-highlighter/dist/esm/styles/prism';
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
        code({ node, inline, className, children, ...props }) {
          const match = /language-(\w+)/.exec(className || '');

          return !inline && match ? (
            <SyntaxHighlighter
              language={match[1]}
              PreTag="div"
              style={vs}
              customStyle={{
                background: 'transparent',
                border: 'none',
              }}
              codeTagProps={{
                className: 'pre-code',
              }}
              children={String(children).replace(/\n$/, '')}
              {...props}
            ></SyntaxHighlighter>
          ) : (
            <code className={className} {...props}>
              {children}
            </code>
          );
        },
      }}
    >
      {children}
    </Styled.Root>
  );
};

export default Markdown;
