import React from 'react';

import Header from 'components/Header/Header';
import RecentFeedsContent from 'components/RecentFeedsContent/RecentFeedsContent';
import AsyncBoundary from 'components/AsyncBoundary';
import Styled from './RecentFeeds.styles';

const RecentFeeds = () => {
  return (
    <>
      <Header />
      <Styled.Root>
        <Styled.SectionTitle fontSize="1.75rem">Recent Toys</Styled.SectionTitle>
        <Styled.RecentToysContainer>
          <AsyncBoundary rejectedFallback={<h1>임시 에러 페이지</h1>}>
            <RecentFeedsContent />
          </AsyncBoundary>
        </Styled.RecentToysContainer>
      </Styled.Root>
    </>
  );
};

export default RecentFeeds;
