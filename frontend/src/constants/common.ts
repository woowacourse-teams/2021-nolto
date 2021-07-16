import { FeedStatus } from 'types';

export const STEP_CONVERTER: { [index: string]: string } = {
  [FeedStatus.PROGRESS]: '진행중',
  [FeedStatus.COMPLETE]: '전시중',
};
