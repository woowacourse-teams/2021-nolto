import styled from 'styled-components';

import IconButtonComponent from 'components/@common/IconButton/IconButton';
import Avatar from 'components/@common/Avatar/Avatar';
import { MEDIA_QUERY } from 'constants/mediaQuery';
import ArrowIcon from 'assets/carouselArrow.svg';
import { PALETTE } from 'constants/palette';

const Root = styled.div`
  position: relative;
  width: 100%;

  &:hover {
    & .carousel-button {
      opacity: 1;
    }
  }
`;

const ControlContainer = styled.div`
  position: absolute;
  display: flex;
  justify-content: space-between;
  align-items: center;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  padding: 0 3rem;
  pointer-events: none;

  &::before {
    content: '';
    display: block;
    position: absolute;
    height: 100%;
    width: 5rem;
    background: linear-gradient(to right, transparent, ${PALETTE.WHITE_400});
    top: 0;
    right: 0;
  }
`;

const HotToyCardsContainer = styled.ul<{ position: number }>`
  grid-row: 1 / 2;
  grid-column: 1 / 8;
  height: 25rem;

  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  transform-style: preserve-3d;
  perspective: 600px;
  --items: 5;
  --middle: 3;
  --position: ${({ position }) => position};

  @media ${MEDIA_QUERY.TABLET} {
    height: 20rem;
  }

  @media ${MEDIA_QUERY.MOBILE} {
    height: 15rem;
  }
`;

const HotToyCardWrapper = styled.li<{ offset: number; position: number }>`
  position: absolute;
  --r: calc(var(--position) - var(--offset));
  --abs: max(calc(var(--r) * -1), var(--r));
  transition: all 0.5s ease;
  transform: translateX(calc(-300px * var(--r) - 40px));
  --offset: ${({ offset }) => offset};
  cursor: pointer;
`;

const VerticalAvatar = styled(Avatar)`
  margin-bottom: 12px;
`;

export const CarouselArrowButton = styled(IconButtonComponent)`
  width: 3rem;
  height: 3rem;
  opacity: 0;
  transition: opacity 0.2s ease;
  pointer-events: visible;
`;

const CarouselLeft = styled(ArrowIcon)`
  transform: rotate(180deg);
`;

const CarouselRight = styled(ArrowIcon)``;

export default {
  Root,
  ControlContainer,
  HotToyCardsContainer,
  HotToyCardWrapper,
  VerticalAvatar,
  CarouselLeft,
  CarouselRight,
};
