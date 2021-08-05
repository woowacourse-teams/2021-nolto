import React from 'react';

import { Author } from 'types';
import CommentForm from './CommentForm';

export default {
  title: 'components/commentBox/CommentForm',
  component: CommentForm,
  argTypes: {},
};

const author: Author = {
  nickname: '미키',
  id: 1,
  imageUrl:
    'https://avatars.githubusercontent.com/u/48755175?s=400&u=1dbaae3d7765dba9692d9b8eb35c5a6bc7c2b5b1&v=4',
};

export const ReplyCommentForm = () => <CommentForm author={author} />;
export const RootCommentForm = () => <CommentForm author={author} isRoot={true} />;
