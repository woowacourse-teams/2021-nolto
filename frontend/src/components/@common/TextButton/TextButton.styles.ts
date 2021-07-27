import styled, { css } from 'styled-components';

import { PALETTE } from 'constants/palette';
import { ButtonStyle } from 'types';

interface RootProps {
  buttonStyle: ButtonStyle;
  reverse?: boolean;
}

const solid = (reverse: boolean) => css`
  background-color: ${reverse ? PALETTE.WHITE_400 : PALETTE.PRIMARY_400};
  border: none;
  color: ${reverse ? PALETTE.PRIMARY_400 : PALETTE.WHITE_400};
`;

const outline = (reverse: boolean) => css`
  background-color: transparent;
  border: 2px solid ${reverse ? PALETTE.WHITE_400 : PALETTE.PRIMARY_400};
  color: ${reverse ? PALETTE.WHITE_400 : PALETTE.PRIMARY_400};
`;

const styleMap = {
  [ButtonStyle.SOLID]: solid,
  [ButtonStyle.OUTLINE]: outline,
};

const Root = styled.button<RootProps>`
  ${({ buttonStyle, reverse }) => styleMap[buttonStyle](reverse)}
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
