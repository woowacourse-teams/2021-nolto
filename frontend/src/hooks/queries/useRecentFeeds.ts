import { QueryFunctionContext, QueryKey, useQuery, UseQueryOptions } from 'react-query';

import api from 'constants/api';
import HttpError from 'utils/HttpError';
import { Feed, FilterType } from 'types';

const getRecentFeeds = async ({ queryKey }: QueryFunctionContext<QueryKey, FilterType>) => {
  const [_, filter] = queryKey;
  const { data } = await api.get('/feeds/recent', { params: { filter } });

  return data;
};

export default function useRecentFeeds(
  option: UseQueryOptions<Feed[], HttpError>,
  filter?: FilterType,
) {
  return useQuery<Feed[]>(['recentFeeds', filter], getRecentFeeds, option);
}
