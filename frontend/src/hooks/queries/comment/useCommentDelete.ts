import { useMutation, UseMutationOptions } from 'react-query';
import { AxiosResponse } from 'axios';

import api from 'constants/api';
import HttpError from 'utils/HttpError';
import { resolveHttpError } from 'utils/error';

interface Args {
  feedId: number;
  commentId: number;
}

const deleteComment = async ({ feedId, commentId }: Args) => {
  try {
    const { data } = await api.delete(`/feeds/${feedId}/comments/${commentId}`);

    return data;
  } catch (error) {
    resolveHttpError({
      error,
      defaultErrorMessage: '댓글 삭제 과정에서 에러가 발생했습니다',
    });
  }
};

const useCommentDelete = (
  { feedId, commentId }: Args,
  option?: UseMutationOptions<AxiosResponse<unknown>, HttpError>,
) => {
  return useMutation<AxiosResponse<unknown>, HttpError>(
    () => deleteComment({ feedId, commentId }),
    option,
  );
};

export default useCommentDelete;
