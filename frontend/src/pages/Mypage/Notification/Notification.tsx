import React, { useState } from 'react';
import { useHistory } from 'react-router-dom';

import CheckMark from 'assets/check.svg';
import CrossMark from 'assets/crossMark.svg';
import { PALETTE } from 'constants/palette';
import useSnackbar from 'contexts/snackbar/useSnackbar';
import useNotiLoad from 'hooks/queries/profile/useNotiLoad';
import useNotiDelete from 'hooks/queries/profile/useNotiDelete';
import useMember from 'hooks/queries/useMember';
import ROUTE from 'constants/routes';
import { NotiType, NotificationType } from 'types';
import Styled, { MoreNotiIcon } from './Notification.styles';

const NotiTypeText = {
  [NotiType.COMMENT_SOS]: '에 도움🙌 을 제안했어요! ',
  [NotiType.COMMENT]: '에 댓글📮 을 남겼습니다. ',
  [NotiType.REPLY]: '의 댓글에 답글💌 을 남겼습니다. ',
  [NotiType.LIKE]: '를 좋아합니다. 👍',
};

const Notification = () => {
  const [moreNotiFolded, setMoreNotiFolded] = useState(true);

  const history = useHistory();
  const snackbar = useSnackbar();

  const { data: notiData, refetch: refetchNoti } = useNotiLoad({
    errorHandler: (error) => {
      snackbar.addSnackbar('error', error.message);
    },
  });

  const { refetchMember } = useMember();

  const deleteMutation = useNotiDelete();

  const handleClickNoti = (data: NotificationType) => {
    const { feed, comment, id } = data;

    history.push({
      pathname: `${ROUTE.FEEDS}/${feed.id}`,
      state: { commentId: comment?.id },
    });

    deleteNoti(id);
  };

  const deleteNoti = (notificationId?: number, event?: React.MouseEvent) => {
    event?.stopPropagation();

    deleteMutation.mutate(
      { notificationId },
      {
        onSuccess: () => {
          refetchMember();
          refetchNoti({ throwOnError: true });
        },
        onError: (error) => {
          snackbar.addSnackbar('error', error.message);
        },
      },
    );
  };

  const defaultNotiCountToShow = 3;

  const notifications = moreNotiFolded ? notiData.slice(0, defaultNotiCountToShow) : notiData;

  const noNotiContent: React.ReactNode = (
    <Styled.NoNotiContent>
      <span>🧐 새로운 알림이 없습니다.</span>
    </Styled.NoNotiContent>
  );

  return (
    <Styled.Root isFolded={moreNotiFolded} notiCount={notiData.length}>
      <Styled.TopContainer>
        <Styled.TitleWrapper>
          <Styled.Title>알림</Styled.Title>
          <Styled.NotiMark>{notiData.length}</Styled.NotiMark>
        </Styled.TitleWrapper>
        {notiData.length > 0 && (
          <Styled.AllReadButton onClick={() => deleteNoti()}>
            모두 읽음으로 표시
            <CheckMark width="14px" />
          </Styled.AllReadButton>
        )}
      </Styled.TopContainer>

      <Styled.NotiContainer>
        {notiData.length > 0
          ? notifications.map((data) => (
              <Styled.NotiWrapper key={data.id} onClick={() => handleClickNoti(data)}>
                <Styled.NotiUserImage src={data.user.imageUrl} />
                <Styled.NotiText>
                  <Styled.NotiBold className="user-name">
                    {data.user.nickname}&nbsp;
                  </Styled.NotiBold>
                  님이&nbsp;
                  <Styled.NotiBold className="feed-title">{data.feed.title}&nbsp;</Styled.NotiBold>
                  프로젝트{NotiTypeText[data.type]}
                </Styled.NotiText>
                <Styled.DeleteNotiButton onClick={(event) => deleteNoti(data.id, event)}>
                  <CrossMark width="12px" fill={PALETTE.BLACK_200} />
                </Styled.DeleteNotiButton>
              </Styled.NotiWrapper>
            ))
          : noNotiContent}
      </Styled.NotiContainer>

      {notiData.length > defaultNotiCountToShow && (
        <Styled.MoreNotiButton onClick={() => setMoreNotiFolded(!moreNotiFolded)}>
          <MoreNotiIcon width="8px" fill={PALETTE.ORANGE_400} isFolded={moreNotiFolded} />
          <span>알림 더보기</span>
        </Styled.MoreNotiButton>
      )}
    </Styled.Root>
  );
};

export default Notification;
