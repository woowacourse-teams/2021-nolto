import React from 'react';

interface Props {
  renderFallback: React.ReactNode;
}

interface State {
  hasError: boolean;
}

class ErrorBoundary extends React.Component<Props, State> {
  state = { hasError: false };

  static getDerivedStateFromError() {
    return { hasError: true };
  }

  render() {
    if (this.state.hasError) {
      return this.props.renderFallback;
    }

    return this.props.children;
  }
}

export default ErrorBoundary;
