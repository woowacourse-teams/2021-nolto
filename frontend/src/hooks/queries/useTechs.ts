import { QueryFunctionContext, QueryKey, useQuery } from 'react-query';

import api from 'constants/api';
import { Tech } from 'types';

const getTechs = async ({ queryKey }: QueryFunctionContext<QueryKey, string>) => {
  const [_, techs] = queryKey;
  const { data } = await api.get(`tags/techs/search?names=${techs}`);

  return data;
};

export default function useTechs(techs: string) {
  return useQuery<Tech[]>(['techs', techs], getTechs);
}
