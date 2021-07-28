import { useQuery, UseQueryOptions } from 'react-query';

import api from 'constants/api';
import { ErrorHandler, Tech } from 'types';
import HttpError from 'utils/HttpError';
import ERROR_CODE from 'constants/errorCode';

interface CustomQueryOption extends UseQueryOptions<Tech[], HttpError> {
  errorHandler?: ErrorHandler;
  techs: string;
}

const getTechs = async (techs: string, errorHandler: ErrorHandler) => {
  try {
    const { data } = await api.get(`tags/techs/search?names=${techs}`);

    return data;
  } catch (error) {
    const { status, data } = error.response;

    console.error(data.errorMessage);

    throw new HttpError(
      status,
      ERROR_CODE[data.errorCode] || '기술스택을 불러오는 과정에서 에러가 발생했습니다',
      errorHandler,
    );
  }
};

const useTechs = ({ techs, errorHandler, ...option }: CustomQueryOption) => {
  return useQuery<Tech[]>(['techs', techs], () => getTechs(techs, errorHandler), option);
};

export default useTechs;
