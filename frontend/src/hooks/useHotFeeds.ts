import { useQuery } from 'react-query';

import api from 'constants/api';
import { Feed } from 'types';

const getHotFeeds = async () => {
  const { data } = await api.get('/feeds/hot');
  return data;
};

export default function useFeeds() {
  return useQuery<Feed[]>('hotFeeds', getHotFeeds);
}
