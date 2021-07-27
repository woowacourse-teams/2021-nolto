import { QueryFunctionContext, QueryKey, useQuery, UseQueryOptions } from 'react-query';

import api from 'constants/api';
import { FeedDetail } from 'types';
import HttpError from 'utils/HttpError';

const getFeedDetail = async ({ queryKey }: QueryFunctionContext<QueryKey>) => {
  const [_, id] = queryKey;
  const { data } = await api.get(`/feeds/${id}`);
  return data;
};

export default function useFeedDetail(id: number, option?: UseQueryOptions<FeedDetail, HttpError>) {
  return useQuery<FeedDetail>(['feedDetail', id], getFeedDetail, option);
}
