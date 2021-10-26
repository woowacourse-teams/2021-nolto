import styled, { keyframes } from 'styled-components';
import { Link } from 'react-router-dom';

import SearchbarComponent from 'components/Searchbar/Searchbar';
import HighLightedText from 'components/@common/HighlightedText/HighlightedText';
import Avatar from 'components/@common/Avatar/Avatar';
import IconButtonComponent from 'components/@common/IconButton/IconButton';
import { FONT_SIZE, Z_INDEX } from 'constants/styles';
import { MEDIA_QUERY } from 'constants/mediaQuery';
import { hoverUnderline } from 'commonStyles';
import ArrowIcon from 'assets/carouselArrow.svg';

const Root = styled.div`
  position: relative;
`;

const SearchContainer = styled.div`
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  z-index: ${Z_INDEX.HOME_SEARCHBAR};
  max-width: 736px;
  margin: 0 auto;
  padding: 0 1rem;

  & > .search-bar {
    z-index: ${Z_INDEX.HOME_SEARCHBAR};
  }
`;

const EllipseWrapper = styled.div`
  position: absolute;
  width: 100%;
  top: -1.5rem;
  left: 0;
`;

const bounce = keyframes`
  0%, 100% {
    transform: translateY(-10%);
  }

  50% {
    transform: translateY(10%);
  }
`;

const SearchTitle = styled.div`
  font-size: 1.5rem;
  color: ${({ theme }) => theme.highLightedText};
  font-weight: ${({ theme }) => theme.titleWeight};
  margin-bottom: 18px;
  animation: ${bounce} 1s linear infinite;

  @media ${MEDIA_QUERY.MOBILE} {
    font-size: 1.25rem;
  }
`;

export const Searchbar = styled(SearchbarComponent)`
  position: relative;
  width: 100%;
  height: 2.5rem;
  margin-bottom: 18px;
`;

const TrendTag = styled.span`
  cursor: pointer;

  > .trends-text {
    ${hoverUnderline};
  }

  > .trends-bar {
    margin-right: 0.75rem;
  }
`;

const ContentArea = styled.div`
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2rem;
  margin-top: 8rem;

  @media ${MEDIA_QUERY.TABLET} {
    margin-top: 6rem;
  }
`;

const TitleWrapper = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  padding: 0 1rem;
`;

const SectionTitle = styled(HighLightedText)`
  font-size: ${FONT_SIZE.LARGE};
`;

const HotToysContainer = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const VerticalAvatar = styled(Avatar)`
  margin-bottom: 12px;
`;

const ToysContainer = styled.div`
  display: flex;
  flex-direction: column;
  width: 100%;
  gap: 1rem;
`;

export const MoreButton = styled(Link)`
  display: inline;
  border: none;
  background: transparent;
  font-size: 1rem;
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
  TrendTag,
  ContentArea,
  TitleWrapper,
  SectionTitle,
  HotToysContainer,
  VerticalAvatar,
  ToysContainer,
  ArrowUp,
};
