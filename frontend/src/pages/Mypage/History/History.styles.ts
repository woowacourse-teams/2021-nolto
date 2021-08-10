import styled from 'styled-components';

import { UserHistoryType } from 'types';
import { PALETTE } from 'constants/palette';
import { hoverLayer } from 'commonStyles';

const Root = styled.div`
  padding: 1rem 2rem;
  width: 36rem;
  height: 24rem;
  border-radius: 0.75rem;
  box-shadow: 4px 4px 8px 4px rgba(85, 85, 85, 0.2);
`;

const SlideBar = styled.div`
  position: relative;
  width: 30rem;
  color: ${PALETTE.ORANGE_400};
  display: flex;
  border-bottom: 1px solid ${PALETTE.ORANGE_400};
  margin: 0 auto;

  /* for slide */
  overflow-x: auto;
  scroll-snap-type: x mandatory;
  scroll-behavior: smooth;
`;

const highlightPixels = {
  [UserHistoryType.MY_FEED]: '0rem',
  [UserHistoryType.MY_COMMENT]: '10rem',
  [UserHistoryType.MY_LIKED]: '20rem',
};

const SlideHighlight = styled.span<{ tab: UserHistoryType }>`
  width: 10rem;
  border-bottom: 3px solid ${PALETTE.ORANGE_400};
  position: absolute;
  bottom: 0;
  left: ${({ tab }) => highlightPixels[tab]};
  transition: left 0.3s ease;
`;

const SlideTitle = styled.span<{ selected: boolean }>`
  color: inherit;
  text-align: center;
  width: 10rem;
  padding: 0.75rem 1rem;
  font-weight: ${({ selected }) => selected && '700'};
  cursor: pointer;

  a {
    color: inherit;
  }
`;

const FeedsSwipeArea = styled.div`
  display: flex;
  height: calc(100% - 3rem);
  overflow-x: auto;
  scroll-behavior: smooth;

  &::-webkit-scrollbar {
    display: none;
  }

  pointer-events: none;

  * {
    pointer-events: auto;
  }
`;

const FeedContainer = styled.div`
  width: 100%;
  margin-top: 0.5rem;
  overflow-y: scroll;

  &::-webkit-scrollbar {
    display: none;
  }

  flex-shrink: 0;
`;

const FeedWrapper = styled.div`
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  cursor: pointer;
  overflow: hidden;

  &:not(:last-of-type) {
    border-bottom: 1px solid ${PALETTE.GRAY_400};
  }

  ${hoverLayer({})};
`;

const FeedThumbnail = styled.img`
  width: 3.5rem;
  height: 3.5rem;
  border-radius: 0.25rem;
`;

const FeedContentWrapper = styled.div`
  width: calc(100% - 2rem);
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

const FeedTitle = styled.div`
  font-weight: 700;
`;

const FeedContent = styled.div`
  font-size: 14px;
  width: calc(100% - 2rem);
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
`;

const FeedComment = styled.div`
  display: flex;
  align-items: center;
  gap: 0.5rem;
`;

const NoFeedContent = styled.div`
  display: flex;
  height: 100%;

  > span {
    margin: auto;
  }
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
  NoFeedContent,
};
