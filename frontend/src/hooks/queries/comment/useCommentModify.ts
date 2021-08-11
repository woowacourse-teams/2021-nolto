import { useMutation, UseMutationOptions } from 'react-query';
import { AxiosResponse } from 'axios';

import api from 'constants/api';
import HttpError from 'utils/HttpError';
import { resolveHttpError } from 'utils/error';
import { CommentRequest, CommentType } from 'types';

interface Args {
  feedId: number;
  commentId: number;
}

const modifyComment =
  ({ feedId, commentId }: Args) =>
  async ({ content, helper }: CommentRequest) => {
    try {
      const { data } = await api.patch(`/feeds/${feedId}/comments/${commentId}`, {
        content,
        helper,
      });

      return data;
    } catch (error) {
      resolveHttpError({
        error,
        defaultErrorMessage: '댓글 수정 과정에서 에러가 발생했습니다',
      });
    }
  };

const useCommentModify = (
  { feedId, commentId }: Args,
  option?: UseMutationOptions<AxiosResponse<CommentType>, HttpError, CommentRequest>,
) => {
  return useMutation<AxiosResponse<CommentType>, HttpError, CommentRequest>(
    modifyComment({ feedId, commentId }),
    option,
  );
};

export default useCommentModify;
