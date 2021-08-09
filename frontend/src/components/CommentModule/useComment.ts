import useSnackbar from 'contexts/snackbar/useSnackbar';
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
  const snackbar = useSnackbar();

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
      onError: (error) => {
        snackbar.addSnackbar('error', error.message);
      },
    },
  );

  const commentDeleteMutation = useCommentDelete(
    { feedId, commentId },
    {
      onSuccess: () => {
        reloadComments();
      },
      onError: (error) => {
        snackbar.addSnackbar('error', error.message);
      },
    },
  );

  const commentLikeMutation = useCommentLike({
    feedId,
    commentId,
    onError: (error) => {
      snackbar.addSnackbar('error', error.message);
    },
  });

  const commentUnlikeMutation = useCommentUnlike({
    feedId,
    commentId,
    onError: (error) => {
      snackbar.addSnackbar('error', error.message);
    },
  });

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
      onError: (error) => {
        snackbar.addSnackbar('error', error.message);
      },
    },
  );

  const subCommentDeleteMutation = useCommentDelete(
    { feedId, commentId },
    {
      onSuccess: () => {
        reloadSubComments();
      },
      onError: (error) => {
        snackbar.addSnackbar('error', error.message);
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
