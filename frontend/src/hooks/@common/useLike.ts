import { useEffect, useState } from 'react';

interface Props {
  initialIsLiked: boolean;
  likeCount: number;
}

const useLike = ({ initialIsLiked, likeCount }: Props) => {
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

  return {
    likeCount: count,
    isLiked,
    setLiked,
  };
};

export default useLike;
