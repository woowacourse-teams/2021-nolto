import React, { useMemo, useState } from 'react';

import useRepliesLoad from 'hooks/queries/comment/reply/useRepliesLoad';
import { CommentBase } from 'types';
import Comment from 'components/CommentBox/Comment/Comment';
import Styled, { FoldButton } from './RepliesProvider.styles';
import useCommentsModule from '../useCommentsModule';

interface Props {
  commentId: number;
  children: React.ReactNode;
}

interface RepliesContext {
  replies: CommentBase[];
  commentId: number;
  reloadReplies: () => void;
}

export const Context = React.createContext<RepliesContext>(null);

const RepliesProvider = ({ children, commentId }: Props) => {
  const { feedId } = useCommentsModule();
  const { data: replies, refetch } = useRepliesLoad({ feedId, commentId });
  const [isFold, setIsFold] = useState(false);

  const handleClickFold = () => {
    setIsFold(!isFold);
  };

  const reloadReplies = () => {
    refetch();
  };

  const repliesContext = useMemo(() => ({ replies, commentId, reloadReplies }), []);

  return (
    <Context.Provider value={repliesContext}>
      {children}
      {replies.length > 0 && (
        <Styled.Reply>
          <FoldButton onClick={handleClickFold}>
            <Styled.ArrowUp isFold={isFold} width="10px" />
            {`답글 ${replies.length}개 ${isFold ? '더보기' : '숨기기'}`}
          </FoldButton>

          <Styled.ReplyWrapper isFold={isFold} replyCount={replies.length}>
            {replies.map((reply) => (
              <Comment comment={reply} />
            ))}
          </Styled.ReplyWrapper>
        </Styled.Reply>
      )}
    </Context.Provider>
  );
};

export default RepliesProvider;
