import React from 'react';

import { CommentBase, RootComment } from 'types';
import { MOCK_USER } from '__mocks__/fixture/User';
import Comment from './Comment';

export default {
  title: 'components/CommentBox/Comment',
  component: Comment,
  argTypes: {},
};

const authorComment: CommentBase = {
  id: 1,
  content: '댓글 내용 아마찌 댓글 내용 아마찌',
  likes: 0,
  liked: false,
  feedAuthor: true,
  createdAt: '2021-08-01T06:46:10.724',
  modified: false,
  author: MOCK_USER.JOEL,
};

const helperComment: CommentBase = {
  id: 1,
  content: '댓글 내용',
  likes: 0,
  liked: false,
  feedAuthor: false,
  createdAt: '2021-08-01T06:46:10.724',
  modified: false,
  author: MOCK_USER.MICKEY,
};

const rootComment: RootComment = {
  id: 1,
  content: '댓글 내용 엄청나게 긴글씨 엄청나게 긴글씨 엄청나게 긴글씨',
  likes: 0,
  liked: false,
  feedAuthor: false,
  createdAt: '2021-08-01T06:46:10.724',
  modified: false,
  author: MOCK_USER.MAZZI,
  helper: true,
  replies: [],
};

export const CommentAuthor = () => <Comment comment={authorComment} />;
export const CommentHelper = () => <Comment comment={helperComment} />;
export const commentRoot = () => <Comment comment={rootComment} />;
