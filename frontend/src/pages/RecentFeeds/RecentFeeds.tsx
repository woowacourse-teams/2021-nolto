import React from 'react';

import BaseLayout from 'components/BaseLayout/BaseLayout';
import RecentFeedsContent from 'components/RecentFeedsContent/RecentFeedsContent';
import AsyncBoundary from 'components/AsyncBoundary';
import ErrorFallback from 'components/ErrorFallback/ErrorFallback';

const RecentFeeds = () => {
  return (
    <BaseLayout>
      <AsyncBoundary rejectedFallback={<ErrorFallback message="최신 피드를 불러올 수 없습니다" />}>
        <RecentFeedsContent />
      </AsyncBoundary>
    </BaseLayout>
  );
};

export default RecentFeeds;
