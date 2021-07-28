import styled, { keyframes } from 'styled-components';

import { PALETTE } from 'constants/palette';
import ArrowUp from 'assets/arrowUp.svg';

const Root = styled.div`
  display: inline-block;
  position: relative;
  width: 100%;
  padding-top: 0.25rem;
`;

const BACKGROUND_COLOR = PALETTE.BLACK_300;
const OPACITY_ANIMATION_TIME = '0.5s';

const opacityAnimation = keyframes`
  from {
    opacity: 0;
  }

  to {
    opacity : 1;
  }
`;

const Message = styled.div`
  position: relative;
  display: flex;
  align-items: center;
  width: 100%;
  height: 1.75rem;
  background-color: ${BACKGROUND_COLOR};
  border-radius: 5px;

  animation: ${opacityAnimation} ${OPACITY_ANIMATION_TIME} ease;
  padding-left: 0.5rem;

  & > span {
    font-size: 0.75rem;
    color: ${PALETTE.WHITE_400};
  }
`;

const ArrowUpIcon = styled(ArrowUp)`
  padding-left: 0.5rem;
  fill: ${BACKGROUND_COLOR};
  display: block;

  animation: ${opacityAnimation} ${OPACITY_ANIMATION_TIME} ease;
`;

export default { Root, Message, ArrowUpIcon };
