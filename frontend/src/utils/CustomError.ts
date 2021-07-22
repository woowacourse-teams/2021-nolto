export default class CustomError extends Error {
  name: string;

  constructor(message?: string) {
    super(message);
    this.name = new.target.name;
    Object.setPrototypeOf(this, new.target.prototype);
    // TODO: 스택트레이스 바로잡기
    // Error.captureStackTrace && Error.captureStackTrace(this, this.constructor);
  }
}
