import React, { useState } from 'react';

import CheckMark from 'assets/check.svg';
import CrossMark from 'assets/crossMark.svg';
import charlieIcon from 'assets/team/charlie.png';
import amazziIcon from 'assets/team/amazzi.png';
import mickeyIcon from 'assets/team/mickey.png';
import Styled, { MoreNotiIcon } from './Notification.styles';
import { PALETTE } from 'constants/palette';
import { NotiType } from 'types';

const mockData = [
  {
    id: 1,
    user: {
      id: 2,
      nickname: 'Charlie',
      imageUrl: charlieIcon,
    },
    feed: {
      id: 1,
      title: '아짜구 저쩌구 아짜구',
    },
    type: NotiType.COMMENT_SOS,
  },
  {
    id: 2,
    user: {
      id: 3,
      nickname: '아마찌',
      imageUrl: amazziIcon,
    },
    feed: {
      id: 2,
      title: '마찌의 개쩌는 지하철 미션',
    },
    type: NotiType.COMMENT,
  },
  {
    id: 3,
    user: {
      id: 7,
      nickname: '믹히',
      imageUrl: mickeyIcon,
    },
    feed: {
      id: 5,
      title: '백신 매크로',
    },
    type: NotiType.LIKE,
  },
  {
    id: 4,
    user: {
      id: 7,
      nickname: '믹히',
      imageUrl: mickeyIcon,
    },
    feed: {
      id: 5,
      title: '백신 매크로',
    },
    type: NotiType.COMMENT,
  },
];

const NotiTypeText = {
  [NotiType.COMMENT_SOS]: '에 도움을 제안했어요!',
  [NotiType.COMMENT]: '에 댓글을 남겼습니다.',
  [NotiType.LIKE]: '를 좋아합니다.',
};

const Notification = () => {
  const [moreNotiFolded, setMoreNotiFolded] = useState(true);

  const defaultNotiCountToShow = 3;

  const notifications = moreNotiFolded ? mockData.slice(0, defaultNotiCountToShow) : mockData;

  return (
    <Styled.Root isFolded={moreNotiFolded} notiCount={mockData.length}>
      <Styled.TopContainer>
        <Styled.TitleWrapper>
          <Styled.Title>알림</Styled.Title>
          <Styled.NotiMark>{mockData.length}</Styled.NotiMark>
        </Styled.TitleWrapper>
        <Styled.AllReadButton>
          모두 읽음으로 표시
          <CheckMark width="14px" />
        </Styled.AllReadButton>
      </Styled.TopContainer>

      <Styled.NotiContainer>
        {notifications.map((data) => (
          <Styled.NotiWrapper key={data.id}>
            <Styled.NotiUserImage src={data.user.imageUrl} />
            <Styled.NotiText>
              <Styled.NotiBold>{data.user.nickname}&nbsp;</Styled.NotiBold>
              님이&nbsp;
              <Styled.NotiBold className="feed-title">{data.feed.title}&nbsp;</Styled.NotiBold>
              프로젝트{NotiTypeText[data.type]}
            </Styled.NotiText>
            <Styled.DeleteNotiButton>
              <CrossMark width="12px" fill={PALETTE.BLACK_200} />
            </Styled.DeleteNotiButton>
          </Styled.NotiWrapper>
        ))}
      </Styled.NotiContainer>

      <Styled.MoreNotiButton onClick={() => setMoreNotiFolded(!moreNotiFolded)}>
        <MoreNotiIcon width="8px" fill={PALETTE.ORANGE_400} isFolded={moreNotiFolded} />
        <span>알림 더보기</span>
      </Styled.MoreNotiButton>
    </Styled.Root>
  );
};

export default Notification;
