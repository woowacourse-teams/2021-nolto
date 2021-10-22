import React from 'react';

import { PALETTE } from 'constants/palette';
import LikeHeartIcon from 'assets/likeHeart.svg';
import FilledLikeHeart from 'assets/filledLikeHeart.svg';
import useLike from 'hooks/@common/useLike';
import useFeedLike from 'hooks/queries/feed/useFeedLike';
import useFeedUnlike from 'hooks/queries/feed/useFeedUnlike';
import LoginModal from 'components/LoginModal/LoginModal';
import useMember from 'contexts/member/useMember';
import useSnackbar from 'contexts/snackbar/useSnackbar';
import useModal from 'contexts/modal/useModal';
import useDialog from 'contexts/dialog/useDialog';
import { ERROR_CODE_KEY, FeedDetail } from 'types';
import Styled, { Button } from './LikeButton.styles';

interface Props {
  feedDetail: FeedDetail;
}

const LikeButton = ({ feedDetail }: Props) => {
  const snackbar = useSnackbar();
  const member = useMember();
  const modal = useModal();
  const dialog = useDialog();
  const { setLiked, isLiked, likeCount } = useLike({
    initialIsLiked: feedDetail.liked,
    likeCount: feedDetail.likes,
  });

  const handleUnauthorizeError = () => {
    member.logout();
    modal.openModal(<LoginModal />);
  };

  const likeMutation = useFeedLike({
    onSuccess: () => {
      setLiked(true);
    },
    onError: (error) => {
      dialog.alert(error.message);

      const errorHandleMap: Partial<Record<ERROR_CODE_KEY, () => void>> = {
        'auth-001': handleUnauthorizeError,
      };

      errorHandleMap[error.errorCode]?.();
    },
  });

  const unlikeMutation = useFeedUnlike({
    onSuccess: () => {
      setLiked(false);
    },
    onError: (error) => {
      dialog.alert(error.message);

      const errorHandleMap: Partial<Record<ERROR_CODE_KEY, () => void>> = {
        'auth-001': handleUnauthorizeError,
      };

      errorHandleMap[error.errorCode]?.();
    },
  });

  const handleToggleLike = () => {
    if (!member.userInfo) {
      snackbar.addSnackbar('error', '로그인이 필요한 서비스입니다.');
      return;
    }

    if (isLiked) {
      unlikeMutation.mutate({ feedId: feedDetail.id });
      return;
    }

    likeMutation.mutate({ feedId: feedDetail.id });
  };

  return (
    <Styled.Root>
      <Button onClick={handleToggleLike}>
        <span className="visually-hidden">좋아요</span>
        {isLiked ? (
          <FilledLikeHeart width="18px" fill={PALETTE.PRIMARY_400} />
        ) : (
          <LikeHeartIcon width="24px" fill={PALETTE.PRIMARY_400} />
        )}
      </Button>
      <span>{likeCount}</span>
    </Styled.Root>
  );
};

export default LikeButton;
