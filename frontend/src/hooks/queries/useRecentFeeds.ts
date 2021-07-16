import { useQuery } from 'react-query';

import api from 'constants/api';
import { Feed } from 'types';

const getRecentFeeds = async () => {
  const { data } = await api.get('/feeds/recent');
  return data;
};

export default function useFeeds() {
  return useQuery<Feed[]>('recentFeeds', getRecentFeeds);
}
