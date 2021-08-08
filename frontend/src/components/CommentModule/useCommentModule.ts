import useCommentsLoad from 'hooks/queries/comment/useCommentsLoad';
import useCommentWrite from 'hooks/queries/comment/useCommentWrite';
import useSubCommentWrite from 'hooks/queries/comment/subComment/useSubCommentWrite';
import useSubCommentsLoad from 'hooks/queries/comment/subComment/useSubCommentsLoad';

interface Props {
  feedId: number;
  parentCommentId?: number;
}

const useCommentModule = ({ feedId, parentCommentId }: Props) => {
  const { data: commentData, refetch: reloadComments } = useCommentsLoad({
    feedId,
    refetchOnWindowFocus: false,
  });

  const commentWriteMutation = useCommentWrite(feedId, {
    onSuccess: () => {
      reloadComments();
    },
  });

  if (parentCommentId === undefined) {
    return {
      data: commentData,
      reload: reloadComments,
      write: commentWriteMutation.mutate,
    };
  }

  const { data: subCommentData, refetch: reloadSubComments } = useSubCommentsLoad({
    feedId,
    parentCommentId,
    refetchOnWindowFocus: false,
    suspense: false,
  });

  const subCommentWriteMutation = useSubCommentWrite(
    { feedId, parentCommentId },
    {
      onSuccess: () => {
        reloadSubComments();
      },
    },
  );

  return {
    data: subCommentData,
    reload: reloadSubComments,
    write: subCommentWriteMutation.mutate,
  };
};

export default useCommentModule;
