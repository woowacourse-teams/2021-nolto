import { QueryFunctionContext, QueryKey, useQuery } from 'react-query';

import api from 'constants/api';
import { FeedDetail } from 'types';

const getFeedDetail = async ({ queryKey }: QueryFunctionContext<QueryKey, number>) => {
  const [_, id] = queryKey;
  const { data } = await api.get(`/feeds/${id}`);
  return data;
};

export default function useFeedDetail(id: number) {
  return useQuery<FeedDetail>(['feedDetail', id], getFeedDetail);
}
