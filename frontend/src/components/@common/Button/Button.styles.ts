import styled, { css } from 'styled-components';

import { PALETTE } from 'constants/palette';
import { ButtonStyle } from 'types';

interface RootProps {
  buttonStyle: ButtonStyle;
  reverse: boolean;
}

const solid = (color: string, reverse: boolean) => css`
  background-color: ${reverse ? PALETTE.WHITE_400 : color};
  border: none;
  color: ${reverse ? color : PALETTE.WHITE_400};
`;

const outline = (color: string, reverse: boolean) => css`
  background-color: transparent;
  border: 2px solid ${reverse ? PALETTE.WHITE_400 : color};
  color: ${reverse ? PALETTE.WHITE_400 : color};
`;

const styleMap = {
  [ButtonStyle.SOLID]: solid,
  [ButtonStyle.OUTLINE]: outline,
};

const Root = styled.button<RootProps>`
  ${({ buttonStyle, reverse }) => styleMap[buttonStyle](PALETTE.PRIMARY_400, reverse)}

  &::after {
    content: '';
    width: '100%';
    height: '100%';
    opacity: 0;
  }

  &::after:hover {
    opacity: 0.5;
  }
`;

export default { Root };
