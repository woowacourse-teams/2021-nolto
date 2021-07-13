import styled from 'styled-components';

import { PALETTE } from 'constants/palette';

const Root = styled.span<{ fontSize: string }>`
  position: relative;
  display: inline-block;
  padding: 0 4px;
  font-size: ${({ fontSize }) => fontSize};
  font-weight: 700;

  &::after {
    content: '';
    position: absolute;
    display: block;
    bottom: 0;
    left: 0;
    background-color: ${PALETTE.HIGHLIGHT};
    opacity: 0.7;
    z-index: -1;
    width: 100%;
    height: 32%;
  }
`;

export default { Root };
