import useMember from 'hooks/queries/useMember';
import { useEffect, useState } from 'react';

interface Props {
  initialIsLiked: boolean;
  likeCount: number;
}

const useLike = ({ initialIsLiked, likeCount }: Props) => {
  const member = useMember();
  const [count, setCount] = useState(likeCount);
  const [isLiked, setIsLiked] = useState(initialIsLiked);

  const setLiked = (isLiked: boolean) => {
    setIsLiked(isLiked);

    if (isLiked) {
      setCount(count + 1);
      return;
    }

    setCount(count - 1);
  };

  useEffect(() => {
    if (likeCount === undefined) return;

    setCount(likeCount);
  }, [likeCount]);

  useEffect(() => {
    if (member.isLogin) {
      setIsLiked(initialIsLiked);
      return;
    }

    setIsLiked(false);
  }, [member.isLogin]);

  return {
    likeCount: count,
    isLiked,
    setLiked,
  };
};

export default useLike;
