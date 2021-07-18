import { FeedStatus } from 'types';

export const STEP_CONVERTER: { [index: string]: string } = {
  [FeedStatus.PROGRESS]: '조립중',
  [FeedStatus.COMPLETE]: '전시중',
};
