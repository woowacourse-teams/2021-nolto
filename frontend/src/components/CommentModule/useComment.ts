import useSubCommentsLoad from 'hooks/queries/comment/subComment/useSubCommentsLoad';
import useCommentDelete from 'hooks/queries/comment/useCommentDelete';
import useCommentLike from 'hooks/queries/comment/useCommentLike';
import useCommentModify from 'hooks/queries/comment/useCommentModify';
import useCommentsLoad from 'hooks/queries/comment/useCommentsLoad';
import useCommentUnlike from 'hooks/queries/comment/useCommentUnlike';

interface Props {
  feedId: number;
  commentId: number;
  parentCommentId?: number;
}

const useComment = ({ feedId, commentId, parentCommentId }: Props) => {
  const { refetch: reloadComments } = useCommentsLoad({
    feedId,
    refetchOnWindowFocus: false,
  });

  const commentModifyMutation = useCommentModify(
    { feedId, commentId },
    {
      onSuccess: () => {
        reloadComments();
      },
      //TODO: 에러처리 해야됨
    },
  );

  const commentDeleteMutation = useCommentDelete(
    { feedId, commentId },
    {
      onSuccess: () => {
        reloadComments();
      },
    },
  );

  const commentLikeMutation = useCommentLike({ feedId, commentId });
  const commentUnlikeMutation = useCommentUnlike({ feedId, commentId });

  if (parentCommentId === undefined) {
    return {
      reload: reloadComments,
      modify: commentModifyMutation.mutate,
      delete: commentDeleteMutation.mutate,
      like: commentLikeMutation.mutate,
      unlike: commentUnlikeMutation.mutate,
    };
  }

  const { refetch: reloadSubComments } = useSubCommentsLoad({
    feedId,
    parentCommentId,
    refetchOnWindowFocus: false,
    suspense: false,
  });

  const subCommentModifyMutation = useCommentModify(
    { feedId, commentId },
    {
      onSuccess: () => {
        reloadSubComments();
      },
      //TODO: 에러처리 해야됨
    },
  );

  const subCommentDeleteMutation = useCommentDelete(
    { feedId, commentId },
    {
      onSuccess: () => {
        reloadSubComments();
      },
    },
  );

  return {
    reload: reloadSubComments,
    modify: subCommentModifyMutation.mutate,
    delete: subCommentDeleteMutation.mutate,
    like: commentLikeMutation.mutate,
    unlike: commentUnlikeMutation.mutate,
  };
};

export default useComment;
