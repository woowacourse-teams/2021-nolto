import ERROR_CODE from 'constants/errorCodeMap';
import CustomError from 'utils/CustomError';

export enum ButtonStyle {
  SOLID = 'SOLID',
  OUTLINE = 'OUTLINE',
}

export enum FeedStatus {
  PROGRESS = 'PROGRESS',
  COMPLETE = 'COMPLETE',
}

export enum FilterType {
  PROGRESS = 'progress',
  COMPLETE = 'complete',
  SOS = 'sos',
}

export enum SearchType {
  CONTENT = '제목/내용',
  TECH = '기술스택',
}

export enum NotiType {
  COMMENT_SOS = 'COMMENT_SOS',
  COMMENT = 'COMMENT',
  LIKE = 'LIKE',
}

export enum UserHistoryType {
  MY_LIKED = 'MY_LIKED',
  MY_FEED = 'MY_FEED',
  MY_COMMENT = 'MY_COMMENT',
}

export interface Author {
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
  author: Author;
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

export interface FeedWithComment {
  feed: Omit<Feed, 'author'>;
  text: string;
}
export interface UserInfo extends Author {
  socialType: 'GOOGLE' | 'GITHUB';
}

export type OAuthType = 'google' | 'github';

export type SnackBarType = 'error' | 'success' | null;

export type AddSnackBar = (type: SnackBarType, text: string) => void;

export type DialogType = 'alert' | 'confirm';

export type ErrorHandler = (error: CustomError) => void;

export interface ErrorResponse {
  status: number;
  data: unknown;
}

export type ERROR_CODE_KEY = keyof typeof ERROR_CODE;

export interface HttpErrorResponse extends ErrorResponse {
  data: {
    message: string;
    errorCode: ERROR_CODE_KEY;
  };
}

export interface CommentBase {
  id: number;
  content: string;
  likes: number;
  liked: boolean;
  feedAuthor: boolean;
  createdAt: string;
  modified: boolean;
  author: Author;
}

export interface RootComment extends CommentBase {
  helper: boolean;
  replies: CommentBase[];
}
