import React, { Suspense } from 'react';

import ErrorBoundary from './ErrorBoundary';
import Loading from 'components/@common/Loading/Loading';

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
