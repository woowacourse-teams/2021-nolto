import React, { useState } from 'react';
import { useHistory } from 'react-router-dom';

import CheckMark from 'assets/check.svg';
import CrossMark from 'assets/crossMark.svg';
import { PALETTE } from 'constants/palette';
import useSnackbar from 'contexts/snackbar/useSnackbar';
import useMember from 'contexts/member/useMember';
import useNotiLoad from 'hooks/queries/profile/useNotiLoad';
import useNotiDelete from 'hooks/queries/profile/useNotiDelete';
import ROUTE from 'constants/routes';
import { NotiType, NotificationType } from 'types';
import Styled, { MoreNotiIcon } from './Notification.styles';

const NotiTypeText = {
  [NotiType.COMMENT_SOS]: 'ė ëėð ė ė ėíėīė! ',
  [NotiType.COMMENT]: 'ė ëęļðŪ ė ëĻęēžėĩëëĪ. ',
  [NotiType.REPLY]: 'ė ëęļė ëĩęļð ė ëĻęēžėĩëëĪ. ',
  [NotiType.LIKE]: 'ëĨž ėĒėíĐëëĪ. ð',
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
      <span>ð§ ėëĄėī ėëĶžėī ėėĩëëĪ.</span>
    </Styled.NoNotiContent>
  );

  return (
    <Styled.Root isFolded={moreNotiFolded} notiCount={notiData.length}>
      <Styled.TopContainer>
        <Styled.TitleWrapper>
          <Styled.Title>ėëĶž</Styled.Title>
          <Styled.NotiMark>{notiData.length}</Styled.NotiMark>
        </Styled.TitleWrapper>
        {notiData.length > 0 && (
          <Styled.AllReadButton onClick={() => deleteNoti()}>
            ëŠĻë ė―ėėžëĄ íė
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
                  <span>ëėī&nbsp;</span>
                  <Styled.NotiBold className="feed-title">{data.feed.title}&nbsp;</Styled.NotiBold>
                  <span>íëĄė íļ{NotiTypeText[data.type]}</span>
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
          <MoreNotiIcon width="16px" fill={PALETTE.PRIMARY_400} isFolded={moreNotiFolded} />
          <span>ėëĶž ëëģīęļ°</span>
        </Styled.MoreNotiButton>
      )}
    </Styled.Root>
  );
};

export default Notification;
