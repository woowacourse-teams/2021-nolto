import React, { Suspense } from 'react';

import Loading from 'components/@common/Loading/Loading';
import ErrorBoundary from './ErrorBoundary';

interface Props {
  pendingFallback?: React.ReactNode;
  rejectedFallback: React.ReactNode;
  children: React.ReactNode;
}

const AsyncBoundary = ({ pendingFallback = <Loading />, rejectedFallback, children }: Props) => {
  return (
    <ErrorBoundary fallback={rejectedFallback}>
      <Suspense fallback={pendingFallback}>{children}</Suspense>
    </ErrorBoundary>
  );
};

export default AsyncBoundary;
