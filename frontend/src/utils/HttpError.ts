import CustomError from './CustomError';
import { ErrorHandler, ERROR_CODE } from 'types';

class HttpError extends CustomError {
  errorCode: ERROR_CODE;

  constructor(errorCode: ERROR_CODE, message?: string, errorHandler?: ErrorHandler) {
    super(message, errorHandler);
    this.name = 'HttpError';
    this.errorCode = errorCode;
  }
}

export default HttpError;
