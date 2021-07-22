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
  step: FeedStatus;
  sos: boolean;
  thumbnailUrl?: string;
}

export interface FeedToUpload extends Omit<Feed, 'id' | 'author' | 'thumbnailUrl'> {
  techs: Tech[];
  storageUrl?: string;
  deployedUrl?: string;
  thumbnailImage: File;
}

export interface FeedDetail extends Feed {
  techs: Tech[];
  storageUrl?: string;
  deployedUrl?: string;
  likes: number;
  views: number;
  liked: boolean;
}

export interface LoginInfo {
  email: string;
  password: string;
}

export interface UserInfo {
  email: string;
}

export type OAuthType = 'google' | 'github';

export type SnackBarType = 'error' | 'success' | null;

export type AddSnackBar = (type: SnackBarType, text: string) => void;
