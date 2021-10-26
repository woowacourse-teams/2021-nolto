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
  height: 360px;

  @media ${MEDIA_QUERY.TABLET} {
    height: 320px;
  }

  @media ${MEDIA_QUERY.MOBILE} {
    height: 296px;
  }

  ellipse {
    filter: ${({ theme }) => theme.headerShadow};

    @media ${MEDIA_QUERY.TABLET} {
      height: 340px;
      cx: 50%;
      cy: 10px;
      rx: 100%;
      ry: 300px;
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
  right: 10%;
  bottom: 13%;
  transform: rotate(-12deg);

  &:hover {
    animation: ${shake} 1s ease infinite;
    animation-direction: alternate;
  }

  @media ${MEDIA_QUERY.TABLET} {
    width: 68px;
    bottom: 20%;
  }

  @media ${MEDIA_QUERY.MOBILE} {
    width: 48px;
    bottom: 10%;
  }
`;

const Pumpkin = styled.img`
  width: 120px;
  height: auto;
  position: absolute;
  right: 8%;
  bottom: 20%;
  cursor: pointer;
  transform: rotate(-12deg);
  transition: transform 0.3s ease;

  &:hover {
    transform: scale(1.1);
  }

  @media ${MEDIA_QUERY.TABLET} {
    display: none;
  }
`;

const bounce = keyframes`
  0%, 100% {
    transform: translateY(-10%);
  }

  50% {
    transform: translateY(10%);
  }
`;

const Witch = styled.img`
  width: 180px;
  position: absolute;
  left: 6%;
  bottom: 15%;
  transform: rotate(12deg);
  visibility: ${({ theme }) => theme.pumpkin};

  animation: ${bounce} 2s linear infinite;
`;

export default { Root, SvgRoot, Horse, Witch, Pumpkin };
