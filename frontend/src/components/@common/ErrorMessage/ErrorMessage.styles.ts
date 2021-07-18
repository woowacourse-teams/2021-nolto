import { PALETTE } from 'constants/palette';
import styled, { keyframes } from 'styled-components';
import ArrowUpSvg from 'assets/arrowUp.svg';

const Root = styled.div`
  display: inline-block;
  position: relative;
  width: 100%;
  padding-top: 1rem;
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

const Background = styled.div`
  position: absolute;
  display: inline-block;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: ${BACKGROUND_COLOR};
  border-radius: 5px;

  animation: ${opacityAnimation} ${OPACITY_ANIMATION_TIME} ease;
`;

const Message = styled.div`
  position: relative;
  display: flex;
  height: 2rem;
  align-items: center;
  padding-left: 0.5rem;

  & > span {
    z-index: 1;
    font-size: 0.75rem;
    color: ${PALETTE.WHITE_400};
  }
`;

const ArrowUpWrapper = styled.div`
  position: absolute;
  padding-left: 0.5rem;
  top: 0;

  & > svg {
    fill: ${BACKGROUND_COLOR};
  }

  animation: ${opacityAnimation} ${OPACITY_ANIMATION_TIME} ease;
`;

const ArrowUp = styled(ArrowUpSvg)``;

export default { Root, Message, Background, ArrowUpWrapper, ArrowUp };
