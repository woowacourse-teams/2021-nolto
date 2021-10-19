import styled from 'styled-components';

import { UserHistoryType } from 'types';
import FeedThumbnailComponent from 'components/Thumbnail/Thumbnail';
import { PALETTE } from 'constants/palette';
import { MEDIA_QUERY } from 'constants/mediaQuery';
import { hoverLayer, defaultShadow } from 'commonStyles';

const Root = styled.div`
  padding: 1rem 2rem;
  width: 100%;
  height: 24rem;
  border-radius: 0.75rem;
  display: flex;
  flex-direction: column;
  ${defaultShadow};

  @media ${MEDIA_QUERY.TABLET} {
    padding: 1rem;
  }
`;

const SlideBar = styled.div`
  position: relative;
  width: 100%;
  color: ${PALETTE.PRIMARY_400};
  display: flex;
  justify-content: space-evenly;
  border-bottom: 1px solid ${PALETTE.PRIMARY_400};
  margin: 0 auto;
`;

const highlightPixels = {
  [UserHistoryType.MY_FEED]: '0%',
  [UserHistoryType.MY_COMMENT]: '33.3%',
  [UserHistoryType.MY_LIKED]: '66.6%',
};

const SlideHighlight = styled.span<{ tab: UserHistoryType }>`
  width: 33.3%;
  border-bottom: 3px solid ${PALETTE.PRIMARY_400};
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

  &:not(:last-of-type) {
    border-bottom: 1px solid ${PALETTE.GRAY_400};
  }

  ${hoverLayer({})};
`;

const FeedThumbnail = styled(FeedThumbnailComponent)`
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
