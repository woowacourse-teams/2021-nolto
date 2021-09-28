import React from 'react';

import BaseLayout from 'components/BaseLayout/BaseLayout';
import RecentFeedsContent from 'components/RecentFeedsContent/RecentFeedsContent';
import AsyncBoundary from 'components/AsyncBoundary';
import ErrorFallback from 'components/ErrorFallback/ErrorFallback';
import Styled, { ScrollUpButton } from './RecentFeeds.styles';
import { PALETTE } from 'constants/palette';

const RecentFeeds = () => {
  const scrollTop = () => {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  return (
    <BaseLayout>
      <AsyncBoundary rejectedFallback={<ErrorFallback message="최신 피드를 불러올 수 없습니다" />}>
        <RecentFeedsContent />
      </AsyncBoundary>
      <ScrollUpButton size="3rem" onClick={scrollTop}>
        <Styled.ArrowUp width="14px" fill={PALETTE.PRIMARY_400} />
      </ScrollUpButton>
    </BaseLayout>
  );
};

export default RecentFeeds;
