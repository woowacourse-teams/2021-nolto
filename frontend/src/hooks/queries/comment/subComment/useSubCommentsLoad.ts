import { useQuery, UseQueryOptions } from 'react-query';

import { backendApi } from 'constants/api';
import QUERY_KEYS from 'constants/queryKeys';
import HttpError from 'utils/HttpError';
import { CommentType, ErrorHandler } from 'types';
import { resolveHttpError } from 'utils/error';

interface CustomQueryOption extends UseQueryOptions<CommentType[], HttpError> {
  feedId: number;
  parentCommentId: number;
  errorHandler?: ErrorHandler;
}

const loadSubComments = async (
  feedId: number,
  parentCommentId: number,
  errorHandler?: ErrorHandler,
) => {
  try {
    const { data } = await backendApi.get(`/feeds/${feedId}/comments/${parentCommentId}/replies`);

    return data;
  } catch (error) {
    resolveHttpError({
      error,
      defaultErrorMessage: '답글 읽기에 에러가 발생했습니다',
      errorHandler,
    });
  }
};

const useSubCommentsLoad = ({
  feedId,
  parentCommentId,
  errorHandler,
  ...option
}: CustomQueryOption) => {
  return useQuery<CommentType[], HttpError>(
    [QUERY_KEYS.SUB_COMMENTS, parentCommentId],
    () => loadSubComments(feedId, parentCommentId, errorHandler),
    option,
  );
};

export default useSubCommentsLoad;
