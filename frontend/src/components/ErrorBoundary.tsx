import { Component, ErrorInfo, ReactNode } from 'react';
import * as Sentry from '@sentry/react';

import CustomError from 'utils/CustomError';
import HttpError from 'utils/HttpError';

interface Props {
  children: ReactNode;
  fallback: ReactNode;
}

interface State {
  hasError: boolean;
}

export default class ErrorBoundary extends Component<Props, State> {
  state: State = {
    hasError: false,
  };

  static getDerivedStateFromError(): State {
    return { hasError: true };
  }

  componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    console.error('Uncaught error in Error Boundary:', error, errorInfo);

    if (error instanceof CustomError) {
      error.executeSideEffect();
    }

    if (!(error instanceof HttpError)) {
      Sentry.showReportDialog();
    }
  }

  render() {
    if (this.state.hasError) {
      return this.props.fallback;
    }

    return this.props.children;
  }
}
