export enum ButtonStyle {
  SOLID = 'SOLID',
  OUTLINE = 'OUTLINE',
}

export enum FeedStatus {
  PROGRESS = 'PROGRESS',
  COMPLETE = 'COMPLETE',
}

export interface User {
  id: number;
  nickname: string;
  imageUrl: string;
}

export interface Feed {
  id: number;
  user: User;
  title: string;
  content: string;
  thumbnailUrl: string;
  sos: boolean;
}
