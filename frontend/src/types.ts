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

export interface Tech {
  id: number;
  text: string;
}

export interface Feed {
  id: number;
  author: User;
  title: string;
  content: string;
  step: string;
  sos: boolean;
  thumbnailImage: File;
}

export interface FeedToUpload extends Omit<Feed, 'id' | 'author'> {
  [index: string]: string | Tech[] | number | boolean | File;
  techs: Tech[];
  storageUrl?: string;
  deployedUrl?: string;
}

export interface FeedDetail extends Feed {
  techs: Tech[];
  storageUrl?: string;
  deployedUrl?: string;
  likes: number;
  views: number;
  liked: boolean;
}
