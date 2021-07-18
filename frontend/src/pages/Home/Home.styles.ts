import styled from 'styled-components';
import { Link } from 'react-router-dom';

import { PALETTE } from 'constants/palette';
import SearchbarComponent from 'components/Searchbar/Searchbar';
import HighLightedText from 'components/@common/HighlightedText/HighlightedText';
import TextButton from 'components/@common/TextButton/TextButton';
import Avatar from 'components/@common/Avatar/Avatar';
import IconButtonComponent from 'components/@common/IconButton/IconButton';
import ArrowIcon from 'assets/carouselArrow.svg';

const Root = styled.div`
  position: relative;
`;

const EllipseWrapper = styled.div`
  position: relative;
  top: -6.75rem;
`;

const SearchContainer = styled.div`
  position: absolute;
  top: 0;
  /* TODO: center 수정 필요 */
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 3.75rem;
`;

const SearchTitle = styled.div`
  font-size: 1.75rem;
  font-weight: 500;
  color: ${PALETTE.WHITE_400};
  margin-bottom: 18px;
`;

export const Searchbar = styled(SearchbarComponent)`
  position: relative;
  width: 30rem;
  height: 2.5rem;
  margin-bottom: 18px;
`;

const TagsContainer = styled.div`
  display: flex;
  gap: 1rem;
`;

const TagButton = styled(TextButton.Rounded)`
  width: 6rem;
  height: 1.5rem;
`;

const ContentArea = styled.div`
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  top: -6.75rem;
  padding: 3rem 10rem;
  text-align: center;
`;

const SectionTitle = styled(HighLightedText)`
  margin-bottom: 48px;
`;

const HotToysContainer = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 128px;
`;

const HotToyCardsContainer = styled.ul<{ position: number }>`
  grid-row: 1 / 2;
  grid-column: 1 / 8;
  width: 100%;
  height: 25rem;
  padding: 0 1rem;

  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  transform-style: preserve-3d;
  perspective: 600px;
  --items: 5;
  --middle: 3;
  --position: ${({ position }) => position};
`;

const HotToyCardWrapper = styled.li<{ offset: number }>`
  position: absolute;
  --r: calc(var(--position) - var(--offset));
  --abs: max(calc(var(--r) * -1), var(--r));
  transition: all 0.5s ease;
  transform: rotateY(calc(-10deg * var(--r))) translateX(calc(-300px * var(--r)));
  z-index: calc((var(--position) - var(--abs)));
  --offset: ${({ offset }) => offset};
  cursor: pointer;
`;

export const CarouselArrowButton = styled(IconButtonComponent)`
  width: 1.85rem;
  height: 1.85rem;
  padding: 0.55rem;
`;

const CarouselLeft = styled(ArrowIcon)`
  transform: rotate(180deg);
`;

const CarouselRight = styled(ArrowIcon)``;

const RecentToysContainer = styled.div`
  display: flex;
  flex-direction: column;
  width: fit-content;
`;

const RecentToyCardsContainer = styled.ul`
  display: flex;
  flex-direction: column;
  gap: 2rem;
  align-items: center;
`;

const VerticalAvatar = styled(Avatar)`
  margin-bottom: 12px;
`;

const LevelButtonsContainer = styled.div`
  display: flex;
  justify-content: center;
  gap: 2rem;
  margin-bottom: 3.5rem;
`;

export const MoreButton = styled(Link)`
  display: inline;
  border: none;
  background: transparent;
  font-size: 1rem;
  margin-top: 36px;
  margin-left: auto;
`;

const ArrowUp = styled(ArrowIcon)`
  transform: rotate(-90deg);
`;

export const ScrollUpButton = styled(IconButtonComponent)`
  width: 2.25rem;
  height: 2.25rem;
  padding: 0.55rem;
  position: fixed;
  right: 1rem;
  bottom: 1rem;
`;

export default {
  Root,
  SearchContainer,
  EllipseWrapper,
  SearchTitle,
  TagsContainer,
  TagButton,
  ContentArea,
  SectionTitle,
  HotToysContainer,
  HotToyCardsContainer,
  HotToyCardWrapper,
  CarouselLeft,
  CarouselRight,
  RecentToysContainer,
  RecentToyCardsContainer,
  VerticalAvatar,
  LevelButtonsContainer,
  ArrowUp,
};
