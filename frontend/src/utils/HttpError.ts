import CustomError from './CustomError';

class HttpError extends CustomError {
  statusCode: number;

  constructor(statusCode: number, message?: string) {
    super(message);
    this.name = 'HttpError';
    this.statusCode = statusCode;
  }
}

export default HttpError;
