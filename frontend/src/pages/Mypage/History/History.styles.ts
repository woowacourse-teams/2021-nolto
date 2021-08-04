import styled from 'styled-components';

import { UserHistoryType } from 'types';
import { PALETTE } from 'constants/palette';

const Root = styled.div`
  padding: 1rem 2rem;
  width: 32rem;
  height: 24rem;
  border-radius: 0.75rem;
  box-shadow: 4px 4px 8px 4px rgba(85, 85, 85, 0.2);
`;

const SlideBar = styled.div`
  position: relative;
  width: 27rem;
  color: ${PALETTE.ORANGE_400};
  display: flex;
  border-bottom: 1px solid ${PALETTE.ORANGE_400};

  /* for slide */
  overflow-x: auto;
  scroll-snap-type: x mandatory;
  scroll-behavior: smooth;
`;

const highlightPixels = {
  [UserHistoryType.MY_LIKED]: '0rem',
  [UserHistoryType.MY_FEED]: '9rem',
  [UserHistoryType.MY_COMMENT]: '18rem',
};

const SlideHighlight = styled.span<{ tab: UserHistoryType }>`
  width: 9rem;
  border-bottom: 3px solid ${PALETTE.ORANGE_400};
  position: absolute;
  bottom: 0;
  left: ${({ tab }) => highlightPixels[tab]};
  transition: left 0.3s ease;
`;

const SlideTitle = styled.span<{ selected: boolean }>`
  color: inherit;
  text-align: center;
  width: 9rem;
  padding: 0.75rem 1.5rem;
  font-weight: ${({ selected }) => selected && '700'};
  cursor: pointer;

  a {
    color: inherit;
  }
`;

const FeedsSwipeArea = styled.div`
  display: flex;
  overflow-x: auto;
  scroll-snap-type: x mandatory;
  scroll-behavior: smooth;

  &::-webkit-scrollbar {
    display: none;
  }

  /* Hide scrollbar for IE, Edge and Firefox */
  -ms-overflow-style: none;
  scrollbar-width: none;
  pointer-events: none;

  * {
    pointer-events: auto;
  }
`;

const FeedContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  margin-top: 0.5rem;

  scroll-snap-align: start;
  flex-shrink: 0;
`;

const FeedWrapper = styled.div`
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  cursor: pointer;
  position: relative;
  overflow: hidden;

  &:not(:last-of-type) {
    border-bottom: 1px solid ${PALETTE.GRAY_400};
  }

  &:hover::after {
    content: '';
    display: block;
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    opacity: 0.1;
    background-color: ${PALETTE.BLACK_400};
  }
`;

const FeedThumbnail = styled.img`
  width: 3.5rem;
  height: 3.5rem;
  border-radius: 0.25rem;
`;

const FeedContentWrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

const FeedTitle = styled.div`
  font-weight: 700;
`;

const FeedContent = styled.div`
  font-size: 14px;
`;

const FeedComment = styled.div`
  display: flex;
  align-items: center;
  gap: 0.5rem;
`;

export default {
  Root,
  SlideBar,
  SlideHighlight,
  SlideTitle,
  FeedsSwipeArea,
  FeedContainer,
  FeedWrapper,
  FeedThumbnail,
  FeedContentWrapper,
  FeedTitle,
  FeedContent,
  FeedComment,
};
