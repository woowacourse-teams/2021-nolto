import styled from 'styled-components';

import { PALETTE } from 'constants/palette';

const Root = styled.button<{ isShadow: boolean }>`
  display: flex;
  width: fit-content;
  flex-shrink: 0;
  justify-content: center;
  align-items: center;
  background-color: ${PALETTE.WHITE_400};
  border: none;
  border-radius: 50%;
  filter: ${({ isShadow }) => isShadow && 'drop-shadow(0 0 4px rgba(0, 0, 0, 0.25))'};
  position: relative;
  overflow: hidden;
  & > * {
    padding: 15%;
    flex-shrink: 0;
  }

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
