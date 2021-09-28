import styled from 'styled-components';

import IconButtonComponent from 'components/@common/IconButton/IconButton';
import Avatar from 'components/@common/Avatar/Avatar';
import { MEDIA_QUERY } from 'constants/mediaQuery';
import ArrowIcon from 'assets/carouselArrow.svg';
import { PALETTE } from 'constants/palette';

const Root = styled.div`
  position: relative;
  overflow: hidden;
  width: 100%;

  &:hover {
    & .carousel-button {
      opacity: 1;
    }
  }

  @media ${MEDIA_QUERY.TABLET} {
    & > .card-container {
      transform: none;
    }

    & > .control-container {
      display: none;
    }

    & > .dot-container {
      display: none;
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
  padding: 0 2rem;
  pointer-events: none;
`;

const HotToyCardsContainer = styled.ul<{ position: number }>`
  display: flex;
  align-items: center;
  position: relative;
  width: 100%;
  --feed-width: 33.4%;
  transition: all 0.3s ease;
  transform: translateX(calc(-1 * var(--feed-width) * ${({ position }) => position}));
  margin-bottom: 1rem;

  & .hot-feed {
    flex-shrink: 0;
    width: var(--feed-width);
  }

  @media ${MEDIA_QUERY.TABLET} {
    overflow: auto;
    transform: none;
  }
`;

const HotToyCardWrapper = styled.li<{ offset: number; position: number }>`
  padding-right: 1rem;
  min-width: 14rem;
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

const Dot = styled.span<{ selected?: boolean }>`
  display: block;
  width: 0.7rem;
  height: 0.7rem;
  background-color: ${({ selected }) => (selected ? PALETTE.GRAY_500 : PALETTE.GRAY_300)};
  border-radius: 50%;
`;

export default {
  Root,
  ControlContainer,
  HotToyCardsContainer,
  HotToyCardWrapper,
  VerticalAvatar,
  CarouselLeft,
  CarouselRight,
  Dot,
};
