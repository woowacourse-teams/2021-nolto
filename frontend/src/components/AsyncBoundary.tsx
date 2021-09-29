import React, { Suspense } from 'react';

import Loading from 'components/@common/Loading/Loading';
import ErrorBoundary from './ErrorBoundary';
import hasWindow from 'constants/windowDetector';

interface Props {
  pendingFallback?: React.ReactNode;
  rejectedFallback: React.ReactNode;
  children: React.ReactNode;
}

const AsyncBoundary = ({ pendingFallback = <Loading />, rejectedFallback, children }: Props) => {
  if (!hasWindow) {
    return <ErrorBoundary fallback={rejectedFallback}>{children}</ErrorBoundary>;
  }

  return (
    <ErrorBoundary fallback={rejectedFallback}>
      <Suspense fallback={pendingFallback}>{children}</Suspense>
    </ErrorBoundary>
  );
};

export default AsyncBoundary;
