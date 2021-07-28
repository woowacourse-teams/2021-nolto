import styled, { keyframes } from 'styled-components';

import HorseIcon from 'assets/horse.svg';

const shake = keyframes`
  0% {
    transform: rotate(-12deg);
  }

  50% {
    transform: rotate(30deg);
  }

  100% {
    transform: rotate(-30deg);
  }
`;

const Root = styled.div`
  position: relative;
`;

const SvgRoot = styled.svg`
  ellipse {
    filter: drop-shadow(0 4px 4px rgba(0, 0, 0, 0.25));
  }
`;

const Horse = styled(HorseIcon)`
  height: auto;
  position: absolute;
  right: 20%;
  bottom: 10%;
  transform: rotate(-12deg);

  &:hover {
    animation: ${shake} 1s ease infinite;
    animation-direction: alternate;
  }
`;

export default { Root, SvgRoot, Horse };
