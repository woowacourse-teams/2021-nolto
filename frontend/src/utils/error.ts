import ERROR_CODE_MAP from 'constants/errorCodeMap';
import { ErrorHandler } from 'types';
import CustomError from './CustomError';
import HttpError from './HttpError';
import { isHttpErrorResponse } from './typeGuard';

interface ResolveHttpErrorResponseArgs {
  errorResponse: any;
  defaultErrorMessage: string;
  errorHandler?: ErrorHandler;
}

export const resolveHttpErrorResponse = ({
  errorResponse,
  defaultErrorMessage,
  errorHandler,
}: ResolveHttpErrorResponseArgs) => {
  if (!isHttpErrorResponse(errorResponse)) {
    throw new CustomError('에러 응답이 ErrorResponse 타입이 아닙니다');
  }

  const { data } = errorResponse;

  console.error(data.message);

  throw new HttpError(
    data.errorCode,
    ERROR_CODE_MAP[data.errorCode] || defaultErrorMessage,
    errorHandler,
  );
};
