import ERROR_CODE from 'constants/errorCodeMap';
import { ErrorHandler } from 'types';
import CustomError from './CustomError';
import HttpError from './HttpError';
import { isHttpErrorResponse } from './typeGuard';

interface ResolveHttpErrorResponseArgs {
  errorResponse: unknown;
  defaultErrorMessage: string;
  errorHandler?: ErrorHandler;
}

export const resolveHttpErrorResponse = ({
  errorResponse,
  defaultErrorMessage,
  errorHandler,
}: ResolveHttpErrorResponseArgs) => {
  if (!isHttpErrorResponse(errorResponse)) {
    console.error('에러 응답이 ErrorResponse 타입이 아닙니다');

    throw new CustomError(defaultErrorMessage, errorHandler);
  }

  const { data } = errorResponse;

  console.error(data.message);

  throw new HttpError(
    data.errorCode,
    ERROR_CODE[data.errorCode] || defaultErrorMessage,
    errorHandler,
  );
};
