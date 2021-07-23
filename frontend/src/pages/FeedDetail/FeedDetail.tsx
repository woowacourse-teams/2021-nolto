import React from 'react';
import { useParams } from 'react-router-dom';

import Header from 'components/Header/Header';
import Styled from './FeedDetail.styles';
import FeedDetailContent from 'components/FeedDetailContent/FeedDetailContent';
import AsyncBoundary from 'components/AsyncBoundary';

const FeedDetail = () => {
  const params = useParams<{ id: string }>();
  const id = Number(params.id);

  return (
    <>
      <Header />
      <Styled.Root>
        <AsyncBoundary rejectedFallback={<div>존재하지 않는 게시물입니닷 (임시)</div>}>
          <FeedDetailContent id={id} />
        </AsyncBoundary>
      </Styled.Root>
    </>
  );
};

export default FeedDetail;
