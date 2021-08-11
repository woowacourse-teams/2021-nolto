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
  z-index: ${Z_INDEX.HOME_SEARCHBAR};

  @media ${MEDIA_QUERY.TABLET} {
    margin-top: 2rem;
  }

  @media ${MEDIA_QUERY.TABLET} {
    margin-top: 1rem;
  }
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
  top: -6.75rem;
  padding: 3rem 0;
  text-align: center;

  @media ${MEDIA_QUERY.TABLET} {
    padding: 2rem;
  }
`;

const SectionTitle = styled(HighLightedText)`
  margin-bottom: 48px;

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
  margin-bottom: 128px;

  @media ${MEDIA_QUERY.TABLET} {
    margin-bottom: 72px;
  }
`;

const VerticalAvatar = styled(Avatar)`
  margin-bottom: 12px;
`;

const RecentToysContainer = styled.div`
  display: flex;
  flex-direction: column;
`;

export const MoreButton = styled(Link)`
  display: inline;
  border: none;
  background: transparent;
  font-size: 1rem;
  padding: 0 1rem;
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
  TrendContainer,
  TrendTag,
  ContentArea,
  SectionTitle,
  HotToysContainer,
  VerticalAvatar,
  RecentToysContainer,
  ArrowUp,
};
