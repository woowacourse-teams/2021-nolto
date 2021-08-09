import { useMutation, UseMutationOptions } from 'react-query';
import { AxiosResponse } from 'axios';

import api from 'constants/api';
import HttpError from 'utils/HttpError';
import { resolveHttpError } from 'utils/error';

interface Args {
  feedId: number;
  commentId: number;
}

interface CustomMutationOption extends UseMutationOptions<AxiosResponse<unknown>, HttpError> {
  feedId: number;
  commentId: number;
}

const postLike =
  ({ feedId, commentId }: Args) =>
  async () => {
    try {
      const { data } = await api.post(`/feeds/${feedId}/comments/${commentId}/unlike`);

      return data;
    } catch (error) {
      resolveHttpError({
        error,
        defaultErrorMessage: '댓글 좋아요 취소 과정에서 에러가 발생했습니다',
      });
    }
  };

const useCommentUnlike = ({ feedId, commentId, ...option }: CustomMutationOption) => {
  return useMutation(postLike({ feedId, commentId }), option);
};

export default useCommentUnlike;
