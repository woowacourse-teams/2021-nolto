import CustomError from './CustomError';
import { ErrorHandler } from 'types';

class HttpError extends CustomError {
  statusCode: number;

  constructor(statusCode: number, message?: string, errorHandler?: ErrorHandler) {
    super(message, errorHandler);
    this.name = 'HttpError';
    this.statusCode = statusCode;
  }
}

export default HttpError;
