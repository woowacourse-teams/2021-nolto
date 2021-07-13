import { PALETTE } from 'constants/palette';
import styled from 'styled-components';

import Searchbar from 'components/Searchbar/Searchbar';
import HighLightedText from 'components/@common/HighlightedText/HighlightedText';
import TextButton from 'components/@common/TextButton/TextButton';
import Avatar from 'components/@common/Avatar/Avatar';
import CarouselArrow from 'assets/carouselArrow.svg';

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
  font-size: 2rem;
  font-weight: 500;
  color: ${PALETTE.WHITE_400};
  margin-bottom: 18px;
`;

const MainSearchbar = styled(Searchbar)`
  position: relative;
  width: 30rem;
  height: 2.25rem;
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

const HotToyCardsContainer = styled.ul`
  display: flex;
  gap: 5rem;
  justify-content: center;
`;

const CarouselLeft = styled(CarouselArrow)`
  cursor: pointer;
`;

const CarouselRight = styled(CarouselArrow)`
  transform: rotate(180deg);
  cursor: pointer;
`;

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
  gap: 48px;
  margin-bottom: 60px;
`;

const MoreButton = styled.button`
  display: inline;
  border: none;
  background: transparent;
  font-size: 1.125rem;
  margin-top: 36px;
  margin-left: auto;
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
  MainSearchbar,
  HotToysContainer,
  HotToyCardsContainer,
  CarouselLeft,
  CarouselRight,
  RecentToysContainer,
  RecentToyCardsContainer,
  VerticalAvatar,
  LevelButtonsContainer,
  MoreButton,
};
