import React, { useMemo } from 'react';

import CommentForm from 'components/CommentBox/CommentForm/CommentForm';
import useDialog from 'context/dialog/useDialog';
import useSnackBar from 'context/snackBar/useSnackBar';
import useCommentLoad from 'hooks/queries/comment/useCommentLoad';
import useCommentWrite from 'hooks/queries/comment/useCommentWrite';
import { CommentRequest } from 'types';
import CommentBox from 'components/CommentBox/CommentBox';
import Styled from './CommentsProviderModule.styles';

interface Props {
  feedId: number;
}

interface CommentsContext {
  feedId: number;
  reloadComment: () => void;
}

export const Context = React.createContext<CommentsContext>(null);

const CommentsProviderModule = ({ feedId }: Props) => {
  const { data: comments, refetch } = useCommentLoad({ feedId });
  const snackBar = useSnackBar();
  const dialog = useDialog();

  const reloadComment = () => {
    refetch();
  };

  const commentWriteMutation = useCommentWrite(feedId, {
    onSuccess: () => {
      snackBar.addSnackBar('success', '댓글 등록에 성공했습니다');
      reloadComment();
    },
    onError: (error) => {
      //TODO: 에러처리 추가적으로 해줘야함
      dialog.alert(error.message);
    },
  });

  const handleSubmitComment = (commentRequest: CommentRequest) => {
    commentWriteMutation.mutate(commentRequest);
  };

  const commentsContext = useMemo(() => ({ feedId, reloadComment }), []);

  return (
    <div>
      <h3>댓글 {comments.length}개</h3>
      <hr />
      <Styled.CommentContainer>
        <Context.Provider value={commentsContext}>
          <CommentForm onSubmit={handleSubmitComment} isRoot={true} />
          {comments.map((comment) => (
            <CommentBox key={comment.id} commentBoxInfo={comment} />
          ))}
        </Context.Provider>
      </Styled.CommentContainer>
    </div>
  );
};

export default CommentsProviderModule;
