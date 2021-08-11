import CustomError from './CustomError';
import { ErrorHandler, ERROR_CODE_KEY } from 'types';

class HttpError extends CustomError {
  errorCode: ERROR_CODE_KEY;

  constructor(errorCode: ERROR_CODE_KEY, message?: string, errorHandler?: ErrorHandler) {
    super(message, errorHandler);
    this.name = 'HttpError';
    this.errorCode = errorCode;
  }
}

export default HttpError;
