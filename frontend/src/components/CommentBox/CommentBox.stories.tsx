import React from 'react';

import { RootComment } from 'types';
import { MOCK_USER } from '__mocks__/fixture/User';
import CommentBox from './CommentBox';

export default {
  title: 'components/CommentBox',
  component: CommentBox,
};

const commentBoxInfo: RootComment = {
  id: 1,
  content: '댓글 내용',
  likes: 0,
  liked: false,
  feedAuthor: false,
  createdAt: '2021-08-01T06:46:10.724',
  modified: false,
  author: MOCK_USER.MAZZI,
  helper: true,
  replies: [
    {
      id: 1,
      content: '대댓글 내용',
      likes: 0,
      liked: false,
      feedAuthor: false,
      createdAt: '2021-08-01T06:46:10.724',
      modified: false,
      author: MOCK_USER.ZIG,
    },
    {
      id: 2,
      content: '대댓글 내용',
      likes: 0,
      liked: false,
      feedAuthor: false,
      createdAt: '2021-08-01T06:46:10.724',
      modified: false,
      author: MOCK_USER.MAZZI,
    },
  ],
};

const Template = () => (
  <>
    <CommentBox commentBoxInfo={commentBoxInfo} />
    <CommentBox commentBoxInfo={commentBoxInfo} />
  </>
);

export const Default = Template.bind({});
