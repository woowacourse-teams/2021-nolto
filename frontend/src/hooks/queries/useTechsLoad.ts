import { useQuery, UseQueryOptions } from 'react-query';

import api from 'constants/api';
import { ErrorHandler, Tech } from 'types';
import HttpError from 'utils/HttpError';
import { resolveHttpError } from 'utils/error';

interface CustomQueryOption extends UseQueryOptions<Tech[], HttpError> {
  errorHandler?: ErrorHandler;
  techs: string;
}

const loadTechs = async (techs: string, errorHandler: ErrorHandler) => {
  try {
    const { data } = await api.get(`tags/techs/search?names=${techs}`);

    return data;
  } catch (error) {
    resolveHttpError({
      error,
      defaultErrorMessage: '기술스택을 불러오는 과정에서 에러가 발생했습니다',
      errorHandler,
    });
  }
};

const useTechsLoad = ({ techs, errorHandler, ...option }: CustomQueryOption) => {
  return useQuery<Tech[]>(['techs', techs], () => loadTechs(techs, errorHandler), option);
};

export default useTechsLoad;
