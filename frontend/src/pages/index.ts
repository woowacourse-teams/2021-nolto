import loadable from '@loadable/component';

import Home from './Home/Home';

const About = loadable(() => import(/* webpackChunkName: "About" */ 'pages/About/About'));
const Upload = loadable(() => import(/* webpackChunkName: "Upload" */ 'pages/Upload/Upload'));
const OAuth = loadable(() => import(/* webpackChunkName: "OAuth" */ 'pages/OAuth/OAuth'));
const Modify = loadable(() => import(/* webpackChunkName: "Modify" */ 'pages/Modify/Modify'));
const Mypage = loadable(() => import(/* webpackChunkName: "Mypage" */ 'pages/Mypage/Mypage'));
const FeedDetail = loadable(
  () => import(/* webpackChunkName: "FeedDetail" */ 'pages/FeedDetail/FeedDetail'),
);
const RecentFeeds = loadable(
  () => import(/* webpackChunkName: "RecentFeeds" */ 'pages/RecentFeeds/RecentFeeds'),
);
const SearchResult = loadable(
  () => import(/* webpackChunkName: "SearchResult" */ 'pages/SearchResult/SearchResult'),
);

export default {
  Home,
  About,
  Upload,
  OAuth,
  Modify,
  Mypage,
  FeedDetail,
  RecentFeeds,
  SearchResult,
};
