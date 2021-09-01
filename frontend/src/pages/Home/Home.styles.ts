import styled from 'styled-components';
import { Link } from 'react-router-dom';

import SearchBarComponent from 'components/SearchBar/SearchBar';
import HighLightedText from 'components/@common/HighlightedText/HighlightedText';
import Avatar from 'components/@common/Avatar/Avatar';
import IconButtonComponent from 'components/@common/IconButton/IconButton';
import { PALETTE } from 'constants/palette';
import Z_INDEX from 'constants/zIndex';
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
  width: 100%;
  padding-bottom: 3.5rem;

  & > .search-bar {
    z-index: ${Z_INDEX.HOME_SEARCHBAR};
  }

  @media ${MEDIA_QUERY.TABLET} {
    margin-top: 2rem;
  }

  @media ${MEDIA_QUERY.TABLET} {
    margin-top: 1rem;
  }
`;

const EllipseWrapper = styled.div`
  position: absolute;
  width: 100vw;
  top: -1.5rem;
  left: 0;
`;

const SearchTitle = styled.div`
  font-size: 1.75rem;
  font-weight: 500;
  color: ${PALETTE.WHITE_400};
  margin-bottom: 18px;

  @media ${MEDIA_QUERY.TABLET} {
    font-size: 1.5rem;
  }

  @media ${MEDIA_QUERY.MOBILE} {
    font-size: 1rem;
  }
`;

export const SearchBar = styled(SearchBarComponent)`
  position: relative;
  width: 32rem;
  height: 2.5rem;
  margin-bottom: 18px;

  @media ${MEDIA_QUERY.TABLET} {
    width: 28rem;
    height: 2.25rem;
    margin-bottom: 14px;
  }

  @media ${MEDIA_QUERY.MOBILE} {
    width: 20rem;
    height: 2rem;
    margin-bottom: 8px;
  }
`;

const TrendContainer = styled.div`
  display: flex;
  gap: 0.75rem;
  align-items: center;

  & span {
    color: ${PALETTE.WHITE_400};
    line-height: 1rem;
  }

  & span.trends {
    font-weight: 700;
  }
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
  top: -1.5rem;
  padding: 5rem 0;

  @media ${MEDIA_QUERY.TABLET} {
    padding: 2rem;
  }
`;

const TitleWrapper = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
`;

const SectionTitle = styled(HighLightedText)`
  @media ${MEDIA_QUERY.TABLET} {
    margin-bottom: 32px;
  }

  @media ${MEDIA_QUERY.MOBILE} {
    font-size: 1.25rem;
  }
`;

const HotToysContainer = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;

  @media ${MEDIA_QUERY.TABLET} {
    margin-bottom: 72px;
  }
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
  TrendContainer,
  TrendTag,
  ContentArea,
  TitleWrapper,
  SectionTitle,
  HotToysContainer,
  VerticalAvatar,
  ToysContainer,
  ArrowUp,
};
