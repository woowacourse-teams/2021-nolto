import styled from 'styled-components';

import { PALETTE } from 'constants/palette';
import { hoverLayer } from 'commonStyles';

const Root = styled.button<{ size: string; hasHoverAnimation: boolean; hasShadow: boolean }>`
  display: flex;
  position: relative;
  justify-content: center;
  align-items: center;
  background-color: ${PALETTE.WHITE_400};
  border: none;
  border-radius: 50%;
  filter: ${({ hasShadow }) => hasShadow && 'drop-shadow(0 0 4px rgba(0, 0, 0, 0.25))'};
  width: ${({ size }) => size};

  &::before {
    content: '';
    display: block;
    width: 100%;
    height: 100%;
    padding-top: 100%;
  }

  ${({ hasHoverAnimation }) => hasHoverAnimation && hoverLayer({})};
`;

const Icon = styled.div`
  position: absolute;
  display: flex;
  justify-content: center;
  align-items: center;
  top: 50%;
  left: 50%;
  transform: translate3d(-50%, -50%, 0);
  width: 100%;
  height: 100%;

  & > * {
    width: 60%;
    height: 60%;
  }
`;

export default { Root, Icon };
