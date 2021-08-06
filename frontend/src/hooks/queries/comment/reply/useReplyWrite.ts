import { useMutation, UseMutationOptions } from 'react-query';
import { AxiosResponse } from 'axios';

import api from 'constants/api';
import HttpError from 'utils/HttpError';
import { resolveHttpError } from 'utils/error';
import { CommentRequest } from 'types';

interface Args {
  feedId: number;
  commentId: number;
}

const writeReply =
  ({ feedId, commentId }: Args) =>
  async ({ content }: CommentRequest) => {
    try {
      const { data } = await api.post(`/feeds/${feedId}/comments/${commentId}/replies`, {
        content,
      });

      return data;
    } catch (error) {
      resolveHttpError({
        error,
        defaultErrorMessage: '댓글 작성 과정에서 에러가 발생했습니다',
      });
    }
  };

const useReplyWrite = (
  { feedId, commentId }: Args,
  option?: UseMutationOptions<AxiosResponse<unknown>, HttpError, CommentRequest>,
) => {
  return useMutation<AxiosResponse<unknown>, HttpError, CommentRequest>(
    writeReply({ feedId, commentId }),
    option,
  );
};

export default useReplyWrite;
