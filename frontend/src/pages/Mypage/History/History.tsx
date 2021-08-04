import React, { useEffect, useRef, useState } from 'react';

import ReturnArrow from 'assets/arrowReturnRight.svg';
import catImage from 'assets/catError.png';
import charlieIcon from 'assets/team/charlie.png';
import { Feed, FeedWithComment, UserHistoryType } from 'types';
import Styled from './History.styles';

const mockData = {
  likedFeeds: [
    {
      id: 1,
      title: 'title1',
      content: 'content1',
      step: 'PROGRESS',
      sos: true,
      thumbnailUrl: catImage,
    },
    {
      id: 2,
      title: 'title2',
      content: 'content2',
      step: 'COMPLETE',
      sos: false,
      thumbnailUrl: catImage,
    },
  ],
  myFeeds: [
    {
      id: 1,
      title: 'title1',
      content: 'content1',
      step: 'PROGRESS',
      sos: true,
      thumbnailUrl: charlieIcon,
    },
    {
      id: 2,
      title: 'title2',
      content: 'content2',
      step: 'COMPLETE',
      sos: false,
      thumbnailUrl: charlieIcon,
    },
  ],
  myComments: [
    {
      feed: {
        id: 1,
        title: 'title1',
        content: 'content1',
        step: 'PROGRESS',
        sos: true,
        thumbnailUrl: catImage,
      },
      text: '댓글',
    },
    {
      feed: {
        id: 2,
        title: 'title2',
        content: 'content1',
        step: 'PROGRESS',
        sos: true,
        thumbnailUrl: catImage,
      },
      text: '댓글2',
    },
  ],
};

const History = () => {
  const [tab, setTab] = useState<UserHistoryType>(UserHistoryType.MY_LIKED);
  const selectedTab = useRef(null);

  const { likedFeeds, myFeeds, myComments } = mockData;

  useEffect(() => {
    selectedTab?.current?.scrollIntoView();
  }, [selectedTab.current]);

  const feedWithContent = (feed: Omit<Feed, 'author'>): React.ReactNode => (
    <Styled.FeedWrapper key={feed.id}>
      <Styled.FeedThumbnail src={feed.thumbnailUrl} />
      <Styled.FeedContentWrapper>
        <Styled.FeedTitle>{feed.title}</Styled.FeedTitle>
        <Styled.FeedContent>{feed.content}</Styled.FeedContent>
      </Styled.FeedContentWrapper>
    </Styled.FeedWrapper>
  );

  const feedWithComment = (feed: FeedWithComment): React.ReactNode => (
    <Styled.FeedWrapper key={feed.feed.id}>
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

  return (
    <Styled.Root>
      <Styled.SlideBar id="slide-title">
        <Styled.SlideHighlight tab={tab} />
        <Styled.SlideTitle
          selected={tab === UserHistoryType.MY_LIKED}
          onClick={() => setTab(UserHistoryType.MY_LIKED)}
        >
          좋아요한 글
        </Styled.SlideTitle>
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
      </Styled.SlideBar>
      <Styled.FeedsSwipeArea>
        <Styled.FeedContainer
          id={UserHistoryType.MY_LIKED}
          ref={tab === UserHistoryType.MY_LIKED ? selectedTab : null}
        >
          {likedFeeds.map((feed) => feedWithContent(feed as Omit<Feed, 'author'>))}
        </Styled.FeedContainer>
        <Styled.FeedContainer
          id={UserHistoryType.MY_FEED}
          ref={tab === UserHistoryType.MY_FEED ? selectedTab : null}
        >
          {myFeeds.map((feed) => feedWithContent(feed as Omit<Feed, 'author'>))}
        </Styled.FeedContainer>
        <Styled.FeedContainer
          id={UserHistoryType.MY_COMMENT}
          ref={tab === UserHistoryType.MY_COMMENT ? selectedTab : null}
        >
          {myComments.map((feed) => feedWithComment(feed as FeedWithComment))}
        </Styled.FeedContainer>
      </Styled.FeedsSwipeArea>
    </Styled.Root>
  );
};

export default History;
