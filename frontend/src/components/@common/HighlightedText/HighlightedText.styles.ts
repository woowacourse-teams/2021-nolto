import styled from 'styled-components';

import { PALETTE } from 'constants/palette';
import Z_INDEX from 'constants/zIndex';

const Root = styled.span<{ fontSize: string }>`
  position: relative;
  padding: 0 4px;
  font-size: ${({ fontSize }) => fontSize};
  font-weight: 700;
  height: fit-content;

  &::after {
    content: '';
    position: absolute;
    display: block;
    bottom: 0;
    left: 0;
    background-color: ${PALETTE.HIGHLIGHT};
    opacity: 0.7;
    z-index: ${Z_INDEX.HIGHLIGHT_TEXT};
    width: 100%;
    height: 32%;
  }
`;

export default { Root };
