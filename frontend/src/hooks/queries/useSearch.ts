import { useQuery, UseQueryOptions, QueryFunctionContext } from 'react-query';

import api from 'constants/api';
import { Feed } from 'types';
import HttpError from 'utils/HttpError';

interface SearchParams {
  query: string;
  techs: string;
}

const getSearchResult = async ({ queryKey }: QueryFunctionContext) => {
  const [_, { query, techs }] = queryKey as [string, SearchParams];
  const queryString = new URLSearchParams({ query, techs });

  const { data } = await api.get('/feeds/search?' + queryString);

  return data;
};

export default function useSearch(
  searchParams: SearchParams,
  option?: UseQueryOptions<Feed[], HttpError>,
) {
  return useQuery<Feed[]>(['searchResult', searchParams], getSearchResult, option);
}
