import Markdown from 'react-markdown';
import styled, { css } from 'styled-components';

import { PALETTE } from 'constants/palette';

const code = css`
  background-color: ${PALETTE.GRAY_200};
  border-radius: 8px;
  font-size: 0.8rem;
  vertical-align: middle;
`;

const Root = styled(Markdown)`
  white-space: normal;
  line-height: 1.8rem;

  * {
    word-break: break-all;
  }

  pre {
    overflow-x: auto;
    ${code}
    padding: 1rem;

    & code {
      line-height: 1.5rem;
      background-color: rgba(0, 0, 0, 0);
    }
  }

  blockquote {
    border-left: 5px solid ${PALETTE.GRAY_300};
    padding-left: 0.5rem;
    & > p {
      color: ${PALETTE.GRAY_400};
    }
  }

  code {
    ${code}
    color: ${PALETTE.BLACK_400};
    font-weight: 600;
    padding: 0.4rem;
  }

  img {
    max-width: max-content;
    width: 100%;
  }

  h1 {
    font-size: 2rem;
    padding-bottom: 1rem;
    border-bottom: 1px solid ${PALETTE.GRAY_200};
  }

  h2 {
    font-size: 1.7rem;
    padding: 1rem 0;
  }

  h3 {
    font-size: 1.4rem;
    padding: 0.5rem 0;
  }

  h4 {
    font-size: 1.1rem;
    padding: 0.5rem 0;
  }

  em {
    font-style: normal;
    background-color: yellow;
  }

  li {
    list-style: inside;
    margin-left: 1rem;
    padding: 0.25rem;
  }

  ol li {
    list-style: decimal;
  }

  p {
    margin: 1rem 0;
  }
`;

export default { Root };
