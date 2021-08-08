import { useQuery, UseQueryOptions } from 'react-query';

import api from 'constants/api';
import HttpError from 'utils/HttpError';
import { ErrorHandler, CommentType } from 'types';
import { resolveHttpError } from 'utils/error';

interface CustomQueryOption extends UseQueryOptions<CommentType[], HttpError> {
  feedId: number;
  errorHandler?: ErrorHandler;
}

const loadComments = async (feedId: number, errorHandler?: ErrorHandler) => {
  try {
    const { data } = await api.get(`/feeds/${feedId}/comments`);

    return data;
  } catch (error) {
    resolveHttpError({
      error,
      defaultErrorMessage: '댓글 읽기에 에러가 발생했습니다',
      errorHandler,
    });
  }
};

const useCommentsLoad = ({ feedId, errorHandler, ...option }: CustomQueryOption) => {
  return useQuery<CommentType[], HttpError>(
    ['comments', feedId],
    () => loadComments(feedId, errorHandler),
    option,
  );
};

export default useCommentsLoad;
