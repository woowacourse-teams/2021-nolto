import { FeedStatus } from 'types';

export const STEP_CONVERTER: { [index: string]: string } = {
  [FeedStatus.PROGRESS]: '조립중',
  [FeedStatus.COMPLETE]: '전시중',
  [FeedStatus.SOS]: '도움요청',
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
