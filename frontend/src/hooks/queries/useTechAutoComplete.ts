import { useQuery, UseQueryOptions } from 'react-query';

import api from 'constants/api';
import { ErrorHandler, Tech } from 'types';
import HttpError from 'utils/HttpError';
import { resolveHttpErrorResponse } from 'utils/error';

interface CustomQueryOption extends UseQueryOptions<Tech[], HttpError> {
  autoComplete: string;
  errorHandler?: ErrorHandler;
}

const getTechs = async (autoComplete: string, errorHandler: ErrorHandler) => {
  try {
    const { data } = await api.get(`/tags/techs?auto_complete=${autoComplete}`);
    return data;
  } catch (error) {
    resolveHttpErrorResponse({
      errorResponse: error.response,
      defaultErrorMessage: '기술스택 자동완성 과정에서 에러가 발생했습니다',
      errorHandler,
    });
  }
};

const useTechAutoComplete = ({ autoComplete, errorHandler }: CustomQueryOption) => {
  return useQuery<Tech[]>(
    ['techAutoComplete', autoComplete],
    () => getTechs(autoComplete, errorHandler),
    {
      enabled: !!autoComplete,
      suspense: false,
    },
  );
};

export default useTechAutoComplete;
