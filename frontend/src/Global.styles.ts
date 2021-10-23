import { createGlobalStyle } from 'styled-components';

import { visuallyHidden } from 'commonStyles';

const GlobalStyle = createGlobalStyle`  
  html, body {
    overflow: auto;
  }
  
  #root {
    background: ${({ theme }) => theme.background};
    transition: background 0.2s ease;
  }

  * {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    font-family: 'Open Sans', 'Noto Sans KR', sans-serif;
    color: ${({ theme }) => theme.defaultText};

    transition: color 0.2s ease;
  }

  a {
    text-decoration: none;
    cursor: pointer;
  }

  li {
    list-style: none;
  }

  button {
    cursor: pointer;
    outline: none;
  }

  input, textarea {
    outline: none;
  }

  .visually-hidden {
    ${visuallyHidden};
  }

  .query-dev-tools * {
    color: #ffffff
  }
`;

export default GlobalStyle;
