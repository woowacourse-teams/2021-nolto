import { createGlobalStyle } from 'styled-components';

import { PALETTE } from './constants/palette';

const GlobalStyle = createGlobalStyle`  
  html, body {
    overflow: auto;
  }

  * {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    font-family: 'Open Sans', 'Noto Sans KR', sans-serif;
    color: ${PALETTE.BLACK_400};
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

  .query-dev-tools * {
    color: #ffffff
  }
`;

export default GlobalStyle;
