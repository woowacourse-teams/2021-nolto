import { useMutation, UseMutationOptions } from 'react-query';
import { AxiosResponse } from 'axios';

import { backendApi } from 'constants/api';
import HttpError from 'utils/HttpError';
import { resolveHttpError } from 'utils/error';
import { CommentRequest } from 'types';

const writeComment =
  (feedId: number) =>
  async ({ content, helper }: CommentRequest) => {
    try {
      const { data } = await backendApi.post(`/feeds/${feedId}/comments`, {
        content,
        helper,
      });

      return data;
    } catch (error) {
      resolveHttpError({
        error,
        defaultErrorMessage: '댓글 작성 과정에서 에러가 발생했습니다',
      });
    }
  };

const useCommentWrite = (
  feedId: number,
  option?: UseMutationOptions<AxiosResponse<unknown>, HttpError, CommentRequest>,
) => {
  return useMutation<AxiosResponse<unknown>, HttpError, CommentRequest>(
    writeComment(feedId),
    option,
  );
};

export default useCommentWrite;
