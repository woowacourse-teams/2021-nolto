import { QueryFunctionContext, QueryKey, useQuery } from 'react-query';

import api from 'constants/api';
import { Feed, FilterType } from 'types';

const getRecentFeeds = async ({ queryKey }: QueryFunctionContext<QueryKey, FilterType>) => {
  const [_, filter] = queryKey;
  const { data } = await api.get('/feeds/recent', { params: { filter } });

  return data;
};

export default function useRecentFeeds(filter?: FilterType) {
  return useQuery<Feed[]>(['recentFeeds', filter], getRecentFeeds);
}
