import { FeedStep } from 'types';

import defaultFeed from 'assets/defaultFeed.png';

export const STEP_CONVERTER: { [index: string]: string } = {
  [FeedStep.PROGRESS]: '진행중',
  [FeedStep.COMPLETE]: '전시중',
};

export const THUMBNAIL_IMG_EXTENSION = [
  'image/apng',
  'image/bmp',
  'image/gif',
  'image/jpg',
  'image/jpeg',
  'image/pjpeg',
  'image/png',
  'image/svg+xml',
];

export const HEIGHT = {
  HEADER: '4rem',
};

export const DEFAULT_IMG = {
  FEED: defaultFeed,
} as const;

export const RECENT_FEEDS_PER_PAGE = 20;
