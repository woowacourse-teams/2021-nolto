import styled from 'styled-components';

import { PALETTE } from 'constants/palette';

const Root = styled.button`
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: ${PALETTE.WHITE_400};
  border: none;
  border-radius: 25px;
  filter: drop-shadow(0 0 4px rgba(0, 0, 0, 0.25));
  position: relative;
  overflow: hidden;

  &:hover::after {
    content: '';
    display: block;
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    opacity: 0.1;
    background-color: ${PALETTE.BLACK_400};
  }
`;

export default { Root };
