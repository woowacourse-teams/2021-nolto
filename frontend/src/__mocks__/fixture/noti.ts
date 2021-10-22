import { NotificationType, NotiType } from 'types';

export const MOCK_NOTI: NotificationType[] = [
  {
    id: 1,
    user: {
      id: 1,
      nickname: '아마찌',
      imageUrl: 'imageUrl',
    },
    feed: {
      id: 1,
      title: 'title1',
    },
    comment: {
      id: 1,
      text: 'comment1',
    },
    type: NotiType.COMMENT,
  },
  {
    id: 2,
    user: {
      id: 1,
      nickname: '아마찌',
      imageUrl: 'imageUrl',
    },
    feed: {
      id: 1,
      title: 'title1',
    },
    comment: {
      id: 1,
      text: 'comment1',
    },
    type: NotiType.REPLY,
  },
  {
    id: 3,
    user: {
      id: 1,
      nickname: '아마찌',
      imageUrl: 'imageUrl',
    },
    feed: {
      id: 2,
      title: 'title2',
    },
    comment: null,
    type: NotiType.LIKE,
  },
];
