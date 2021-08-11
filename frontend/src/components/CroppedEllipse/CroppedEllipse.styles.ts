import styled, { keyframes } from 'styled-components';

import HorseIcon from 'assets/horse.svg';
import { MEDIA_QUERY } from 'constants/mediaQuery';

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
  height: 400px;

  @media ${MEDIA_QUERY.TABLET} {
    height: 360px;
  }

  @media ${MEDIA_QUERY.MOBILE} {
    height: 296px;
  }

  ellipse {
    filter: drop-shadow(0 4px 4px rgba(0, 0, 0, 0.25));

    @media ${MEDIA_QUERY.TABLET} {
      height: 340px;
      cx: 50%;
      cy: 10px;
      rx: 100%;
      ry: 324px;
    }

    @media ${MEDIA_QUERY.MOBILE} {
      height: 300px;
      cx: 50%;
      cy: 6px;
      rx: 120%;
      ry: 268px;
    }
  }
`;

const Horse = styled(HorseIcon)`
  width: 84px;
  height: auto;
  position: absolute;
  right: 20%;
  bottom: 10%;
  transform: rotate(-12deg);

  &:hover {
    animation: ${shake} 1s ease infinite;
    animation-direction: alternate;
  }

  @media ${MEDIA_QUERY.TABLET} {
    width: 68px;
  }

  @media ${MEDIA_QUERY.MOBILE} {
    width: 48px;
  }
`;

export default { Root, SvgRoot, Horse };
