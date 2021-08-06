import styled, { css } from 'styled-components';

import { PALETTE } from 'constants/palette';
import { hoverLayer } from 'commonStyles';
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
  overflow: hidden;

  ${hoverLayer({})};
`;

export default { Root };
