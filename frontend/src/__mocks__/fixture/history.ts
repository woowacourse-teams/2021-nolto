import { FeedStep, UserHistory } from 'types';

export const MOCK_HISTORY: UserHistory = {
  likedFeeds: [
    {
      id: 1,
      title: 'title1',
      content: 'content1',
      step: FeedStep.PROGRESS,
      sos: true,
      thumbnailUrl: 'www.turl.com',
    },
    {
      id: 2,
      title: 'title2',
      content: 'content2',
      step: FeedStep.COMPLETE,
      sos: false,
      thumbnailUrl: '',
    },
  ],
  myFeeds: [
    {
      id: 1,
      title: 'title1',
      content: 'content1',
      step: FeedStep.PROGRESS,
      sos: true,
      thumbnailUrl: 'www.turl.com',
    },
    {
      id: 2,
      title: 'title2',
      content: 'content2',
      step: FeedStep.COMPLETE,
      sos: false,
      thumbnailUrl: '',
    },
  ],
  myComments: [
    {
      feed: {
        id: 1,
        title: 'title1',
        content: 'content1',
        step: FeedStep.PROGRESS,
        sos: true,
        thumbnailUrl: 'www.turl.com',
      },
      text: 'comment1',
    },
    {
      feed: {
        id: 2,
        title: 'title2',
        content: 'content2',
        step: FeedStep.COMPLETE,
        sos: false,
        thumbnailUrl: '',
      },
      text: 'comment2',
    },
  ],
};
