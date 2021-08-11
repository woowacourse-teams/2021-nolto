import { useMutation, UseMutationOptions } from 'react-query';
import { AxiosResponse } from 'axios';

import api from 'constants/api';
import HttpError from 'utils/HttpError';
import { resolveHttpError } from 'utils/error';
import { CommentRequest } from 'types';

interface Args {
  feedId: number;
  parentCommentId: number;
}

const writeSubComment =
  ({ feedId, parentCommentId }: Args) =>
  async ({ content }: CommentRequest) => {
    try {
      const { data } = await api.post(`/feeds/${feedId}/comments/${parentCommentId}/replies`, {
        content,
      });

      return data;
    } catch (error) {
      resolveHttpError({
        error,
        defaultErrorMessage: '답글 작성 과정에서 에러가 발생했습니다',
      });
    }
  };

const useSubCommentWrite = (
  { feedId, parentCommentId }: Args,
  option?: UseMutationOptions<AxiosResponse<unknown>, HttpError, CommentRequest>,
) => {
  return useMutation<AxiosResponse<unknown>, HttpError, CommentRequest>(
    writeSubComment({ feedId, parentCommentId }),
    option,
  );
};

export default useSubCommentWrite;
