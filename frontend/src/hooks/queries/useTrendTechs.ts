import { useQuery, UseQueryOptions } from 'react-query';

import { backendApi } from 'constants/api';
import QUERY_KEYS from 'constants/queryKeys';
import { ErrorHandler, Tech } from 'types';
import HttpError from 'utils/HttpError';
import { resolveHttpError } from 'utils/error';

interface CustomQueryOption extends UseQueryOptions<Tech[], HttpError> {
  errorHandler?: ErrorHandler;
}

const getTrendTechs = async (errorHandler: ErrorHandler) => {
  try {
    const { data } = await backendApi.get('tags/techs/trend');

    return data;
  } catch (error) {
    resolveHttpError({
      error,
      defaultErrorMessage: '트렌드 기술 스택을 불러오는 과정에서 에러가 발생했습니다',
      errorHandler,
    });
  }
};

const useTrendTechs = ({ errorHandler, ...option }: CustomQueryOption) => {
  return useQuery<Tech[]>(QUERY_KEYS.TREND_TECHS, () => getTrendTechs(errorHandler), option);
};

export default useTrendTechs;
