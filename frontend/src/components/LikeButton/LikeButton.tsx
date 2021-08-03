import React, { useEffect, useState } from 'react';

import { PALETTE } from 'constants/palette';
import { Button } from './LikeButton.styles';
import LikeHeartIcon from 'assets/likeHeart.svg';
import FilledLikeHeart from 'assets/filledLikeHeart.svg';
import useFeedLike from 'hooks/mutations/useFeedLike';
import useFeedUnlike from 'hooks/mutations/useFeedUnlike';
import useMember from 'hooks/queries/useMember';
import LoginModal from 'components/LoginModal/LoginModal';
import useSnackBar from 'context/snackBar/useSnackBar';
import useModal from 'context/modal/useModal';
import useDialog from 'context/dialog/useDialog';
import { ERROR_CODE_KEY, FeedDetail } from 'types';
import Styled from './LikeButton.styles';

interface Props {
  feedDetail: FeedDetail;
}

const LikeButton = ({ feedDetail }: Props) => {
  const [isLiked, setIsLiked] = useState(feedDetail.liked);
  const [likeCount, setLikeCount] = useState(0);

  const snackBar = useSnackBar();
  const member = useMember();
  const modal = useModal();
  const dialog = useDialog();

  const handleUnauthorizeError = () => {
    member.logout();
    modal.openModal(<LoginModal />);
  };

  const likeMutation = useFeedLike({
    onSuccess: () => {
      setIsLiked(true);
      setLikeCount(likeCount + 1);
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
      setIsLiked(false);
      setLikeCount(likeCount - 1);
    },
    onError: (error) => {
      dialog.alert(error.message);

      const errorHandleMap: Partial<Record<ERROR_CODE_KEY, () => void>> = {
        'auth-001': handleUnauthorizeError,
      };

      errorHandleMap[error.errorCode]?.();
    },
  });

  useEffect(() => {
    if (feedDetail.liked === undefined) return;

    setIsLiked(feedDetail.liked);
  }, [feedDetail.liked]);

  useEffect(() => {
    if (feedDetail.likes === undefined) return;

    setLikeCount(feedDetail.likes);
  }, [feedDetail.likes]);

  const handleToggleLike = () => {
    if (!member.userData) {
      snackBar.addSnackBar('error', '로그인이 필요한 서비스입니다.');
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
        {isLiked ? (
          <FilledLikeHeart width="18px" fill={PALETTE.PRIMARY_400} />
        ) : (
          <LikeHeartIcon width="24px" fill={PALETTE.PRIMARY_200} />
        )}
      </Button>
      <span>{likeCount}</span>
    </Styled.Root>
  );
};

export default LikeButton;
