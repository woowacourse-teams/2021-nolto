import { QueryFunctionContext, QueryKey, useQuery } from 'react-query';

import api from 'constants/api';
import { Tech } from 'types';

const getTechs = async ({ queryKey }: QueryFunctionContext<QueryKey, string>) => {
  const [_, autoComplete] = queryKey;

  const { data } = await api.get(`/tags/techs?auto_complete=${autoComplete}`);
  return data;
};

export default function useTechAutoComplete(autoComplete: string) {
  return useQuery<Tech[]>(['techAutoComplete', autoComplete], getTechs, {
    enabled: !!autoComplete,
    suspense: false,
  });
}
