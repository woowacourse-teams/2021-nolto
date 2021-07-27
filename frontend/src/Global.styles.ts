import { createGlobalStyle } from 'styled-components';

import { PALETTE } from './constants/palette';

const GlobalStyle = createGlobalStyle`
  @import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300;400;500;700&display=swap');
  @import url('https://fonts.googleapis.com/css2?family=Work+Sans:ital,wght@1,700&display=swap');

  * {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    font-family: 'Noto Sans KR', sans-serif;
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
