import React, { useEffect, useRef, useState } from 'react';
import { useHistory } from 'react-router-dom';

import useUserHistory from 'hooks/queries/userHistory/useUserHistory';
import useSnackbar from 'contexts/snackbar/useSnackbar';
import ROUTE from 'constants/routes';
import ReturnArrow from 'assets/arrowReturnRight.svg';
import { genNewId } from 'utils/common';
import { Feed, FeedWithComment, UserHistoryType } from 'types';
import Styled from './History.styles';

const History = () => {
  const [tab, setTab] = useState<UserHistoryType>(UserHistoryType.MY_FEED);
  const selectedTab = useRef(null);
  const history = useHistory();

  const snackbar = useSnackbar();

  const { data: historyData } = useUserHistory({
    errorHandler: (error) => {
      snackbar.addSnackbar('error', error.message);
    },
  });

  const goFeedDetail = (feedId: Feed['id']) => {
    history.push(`${ROUTE.FEEDS}/${feedId}`);
  };

  const { likedFeeds, myFeeds, myComments } = historyData;

  const idGenerator = genNewId();

  useEffect(() => {
    selectedTab.current.scrollIntoView();
  }, [tab]);

  const feedWithContent = (feed: Omit<Feed, 'author'>): React.ReactNode => (
    <Styled.FeedWrapper key={idGenerator()} onClick={() => goFeedDetail(feed.id)}>
      <Styled.FeedThumbnail src={feed.thumbnailUrl} />
      <Styled.FeedContentWrapper>
        <Styled.FeedTitle>{feed.title}</Styled.FeedTitle>
        <Styled.FeedContent>{feed.content}</Styled.FeedContent>
      </Styled.FeedContentWrapper>
    </Styled.FeedWrapper>
  );

  const feedWithComment = (feed: FeedWithComment): React.ReactNode => (
    <Styled.FeedWrapper key={idGenerator()} onClick={() => goFeedDetail(feed.feed.id)}>
      <Styled.FeedThumbnail src={feed.feed.thumbnailUrl} />
      <Styled.FeedContentWrapper>
        <Styled.FeedTitle>{feed.feed.title}</Styled.FeedTitle>
        <Styled.FeedComment>
          <ReturnArrow width="14px" />
          {feed.text}
        </Styled.FeedComment>
      </Styled.FeedContentWrapper>
    </Styled.FeedWrapper>
  );

  const noFeedContent: React.ReactNode = (
    <Styled.NoFeedContent>
      <span>🧐 게시글이 없습니다.</span>
    </Styled.NoFeedContent>
  );

  return (
    <Styled.Root>
      <Styled.SlideBar id="slide-title">
        <Styled.SlideHighlight tab={tab} />
        <Styled.SlideTitle
          selected={tab === UserHistoryType.MY_FEED}
          onClick={() => setTab(UserHistoryType.MY_FEED)}
        >
          내가 작성한 글
        </Styled.SlideTitle>
        <Styled.SlideTitle
          selected={tab === UserHistoryType.MY_COMMENT}
          onClick={() => setTab(UserHistoryType.MY_COMMENT)}
        >
          내가 남긴 댓글
        </Styled.SlideTitle>
        <Styled.SlideTitle
          selected={tab === UserHistoryType.MY_LIKED}
          onClick={() => setTab(UserHistoryType.MY_LIKED)}
        >
          좋아요한 글
        </Styled.SlideTitle>
      </Styled.SlideBar>
      <Styled.FeedsSwipeArea>
        <Styled.FeedContainer
          id={UserHistoryType.MY_FEED}
          ref={tab === UserHistoryType.MY_FEED ? selectedTab : null}
        >
          {myFeeds.length > 0
            ? myFeeds.map((feed: Omit<Feed, 'author'>) => feedWithContent(feed))
            : noFeedContent}
        </Styled.FeedContainer>

        <Styled.FeedContainer
          id={UserHistoryType.MY_COMMENT}
          ref={tab === UserHistoryType.MY_COMMENT ? selectedTab : null}
        >
          {myComments.length > 0
            ? myComments.map((feed: FeedWithComment) => feedWithComment(feed))
            : noFeedContent}
        </Styled.FeedContainer>

        <Styled.FeedContainer
          id={UserHistoryType.MY_LIKED}
          ref={tab === UserHistoryType.MY_LIKED ? selectedTab : null}
        >
          {likedFeeds.length > 0
            ? likedFeeds.map((feed: Omit<Feed, 'author'>) => feedWithContent(feed))
            : noFeedContent}
        </Styled.FeedContainer>
      </Styled.FeedsSwipeArea>
    </Styled.Root>
  );
};

export default History;
