import { CommentType } from 'types';
import { MOCK_USER } from './User';

export const MOCK_SUB_COMMENTS: CommentType[] = [
  {
    author: MOCK_USER.JOEL,
    content: '대댓글1',
    createdAt: '2021-08-08T03:01:03',
    feedAuthor: false,
    id: 1,
    liked: false,
    likes: 0,
    modified: true,
    helper: false,
  },
  {
    author: MOCK_USER.POMO,
    content: '대댓글2',
    createdAt: '2021-08-08T03:01:03',
    feedAuthor: false,
    id: 2,
    liked: false,
    likes: 0,
    modified: true,
    helper: false,
  },
  {
    author: MOCK_USER.CHARLIE,
    content: '대댓글3',
    createdAt: '2021-08-08T03:01:03',
    feedAuthor: false,
    id: 3,
    liked: false,
    likes: 0,
    modified: true,
    helper: false,
  },
];

export const MOCK_COMMENTS: CommentType[] = [
  {
    author: MOCK_USER.MICKEY,
    content: '댓글1',
    createdAt: '2021-08-08T03:01:03',
    feedAuthor: false,
    id: 4,
    liked: false,
    likes: 0,
    modified: true,
    helper: false,
  },
  {
    author: MOCK_USER.ZIG,
    content: '댓글2',
    createdAt: '2021-08-08T03:01:03',
    feedAuthor: false,
    id: 5,
    liked: false,
    likes: 0,
    modified: true,
    helper: false,
  },
  {
    author: MOCK_USER.MAZZI,
    content: '댓글3',
    createdAt: '2021-08-08T03:01:03',
    feedAuthor: false,
    id: 6,
    liked: false,
    likes: 0,
    modified: true,
    helper: false,
  },
];
