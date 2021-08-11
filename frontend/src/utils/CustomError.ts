import { ErrorHandler } from 'types';

export default class CustomError extends Error {
  name: string;
  errorHandler: ErrorHandler;

  constructor(message?: string, errorHandler?: ErrorHandler) {
    super(message);
    this.name = new.target.name;
    this.errorHandler = errorHandler;
    Object.setPrototypeOf(this, new.target.prototype);
    // TODO: 스택트레이스 바로잡기
    // Error.captureStackTrace && Error.captureStackTrace(this, this.constructor);
  }

  executeSideEffect() {
    if (this.errorHandler) {
      this.errorHandler(this);
    }
  }
}
