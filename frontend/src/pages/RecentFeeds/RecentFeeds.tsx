import React from 'react';
import { Helmet } from 'react-helmet';

import BaseLayout from 'components/BaseLayout/BaseLayout';
import RecentFeedsContent from 'components/RecentFeedsContent/RecentFeedsContent';
import AsyncBoundary from 'components/AsyncBoundary';
import ErrorFallback from 'components/ErrorFallback/ErrorFallback';
import { PALETTE } from 'constants/palette';
import hasWindow from 'constants/windowDetector';
import Styled, { ScrollUpButton } from './RecentFeeds.styles';

const RecentFeeds = () => {
  const scrollTop = () => {
    if (hasWindow) {
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  };

  return (
    <BaseLayout>
      <Helmet>
        <title>놀토: 최신 피드</title>
        <link rel="canonical" href="https://www.nolto.app/recent" />
      </Helmet>
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
