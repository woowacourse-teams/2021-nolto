import { ErrorHandler } from 'types';

export default class CustomError extends Error {
  errorHandler: ErrorHandler;

  constructor(message?: string, errorHandler?: ErrorHandler) {
    super(message);
    this.name = new.target.name;
    this.errorHandler = errorHandler;
    Object.setPrototypeOf(this, new.target.prototype);
  }

  executeSideEffect() {
    if (this.errorHandler) {
      this.errorHandler(this);
    }
  }
}
