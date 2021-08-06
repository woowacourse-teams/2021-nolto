import { useQuery, UseQueryOptions } from 'react-query';

import api from 'constants/api';
import HttpError from 'utils/HttpError';
import { CommentBase, ErrorHandler } from 'types';
import { resolveHttpError } from 'utils/error';

interface CustomQueryOption extends UseQueryOptions<CommentBase[], HttpError> {
  feedId: number;
  commentId: number;
  errorHandler?: ErrorHandler;
}

const loadReplies = async (feedId: number, commentId: number, errorHandler?: ErrorHandler) => {
  try {
    const { data } = await api.get(`/feeds/${feedId}/comments/${commentId}`);

    return data;
  } catch (error) {
    resolveHttpError({
      error,
      defaultErrorMessage: '댓글 읽기에 에러가 발생했습니다',
      errorHandler,
    });
  }
};

const useRepliesLoad = ({ feedId, commentId, errorHandler, ...option }: CustomQueryOption) => {
  return useQuery<CommentBase[], HttpError>(
    ['replies', commentId],
    () => loadReplies(feedId, commentId, errorHandler),
    option,
  );
};

export default useRepliesLoad;
