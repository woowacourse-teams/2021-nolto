import React, { useMemo } from 'react';

import CommentForm from 'components/CommentBox/CommentForm/CommentForm';
import useDialog from 'context/dialog/useDialog';
import useSnackBar from 'context/snackBar/useSnackBar';
import useCommentsLoad from 'hooks/queries/comment/useCommentsLoad';
import useCommentWrite from 'hooks/queries/comment/useCommentWrite';
import { CommentRequest } from 'types';
import CommentBox from 'components/CommentBox/CommentBox';
import Styled from './CommentsProviderModule.styles';

interface Props {
  feedId: number;
}

interface CommentsContext {
  feedId: number;
  reloadComments: () => void;
}

export const Context = React.createContext<CommentsContext>(null);

const CommentsProviderModule = ({ feedId }: Props) => {
  const { data: comments, refetch } = useCommentsLoad({ feedId });
  const snackBar = useSnackBar();
  const dialog = useDialog();

  const reloadComments = () => {
    refetch();
  };

  const commentWriteMutation = useCommentWrite(feedId, {
    onSuccess: () => {
      snackBar.addSnackBar('success', '댓글 등록에 성공했습니다');
      reloadComments();
    },
    onError: (error) => {
      //TODO: 에러처리 추가적으로 해줘야함
      dialog.alert(error.message);
    },
  });

  const handleSubmitComment = (commentRequest: CommentRequest) => {
    commentWriteMutation.mutate(commentRequest);
  };

  const commentsContext = useMemo(() => ({ feedId, reloadComments }), []);

  return (
    <div>
      <Context.Provider value={commentsContext}>
        <h3>댓글 {comments.length}개</h3>
        <hr />
        <Styled.CommentContainer>
          <CommentForm onSubmit={handleSubmitComment} isRoot={true} />
          {comments.map((comment) => (
            <CommentBox key={comment.id} commentBoxInfo={comment} />
          ))}
        </Styled.CommentContainer>
      </Context.Provider>
    </div>
  );
};

export default CommentsProviderModule;
