import { FeedStep } from 'types';

export const STEP_CONVERTER: { [index: string]: string } = {
  [FeedStep.PROGRESS]: '진행중',
  [FeedStep.COMPLETE]: '전시중',
};

export const THUMBNAIL_EXTENSION = [
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
  FEED: 'https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png',
} as const;
