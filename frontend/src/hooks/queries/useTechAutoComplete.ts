import { QueryFunctionContext, QueryKey, useQuery } from 'react-query';

import api from 'constants/api';
import { Tech } from 'types';

const getTechs = async (autoComplete: string) => {
  const { data } = await api.get(`/tags/techs?auto_complete=${autoComplete}`);

  return data;
};

export default function useTechAutoComplete(autoComplete: string) {
  return useQuery<Tech[]>(['techAutoComplete', autoComplete], () => getTechs(autoComplete), {
    enabled: !!autoComplete,
    suspense: false,
  });
}
