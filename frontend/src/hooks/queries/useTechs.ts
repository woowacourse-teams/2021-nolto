import { useQuery } from 'react-query';

import api from 'constants/api';
import { Tech } from 'types';

const getTechs = async (techs: string) => {
  const { data } = await api.get(`tags/techs/search?names=${techs}`);

  return data;
};

export default function useTechs(techs: string) {
  return useQuery<Tech[]>(['techs', techs], () => getTechs(techs));
}
