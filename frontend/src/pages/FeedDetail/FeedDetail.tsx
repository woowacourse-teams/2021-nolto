import React from 'react';
import { useParams } from 'react-router-dom';

import Header from 'components/Header/Header';
import FeedDetailContent from 'pages/FeedDetail/FeedDetailContent/FeedDetailContent';
import AsyncBoundary from 'components/AsyncBoundary';
import ErrorFallback from 'components/ErrorFallback/ErrorFallback';
import Styled from './FeedDetail.styles';

const FeedDetail = () => {
  const params = useParams<{ id: string }>();
  const id = Number(params.id);

  return (
    <>
      <Header />
      <Styled.Root>
        <AsyncBoundary
          rejectedFallback={
            <ErrorFallback message="데이터를 불러올 수 없습니다." queryKey="feedDetail" />
          }
        >
          <FeedDetailContent id={id} />
        </AsyncBoundary>
      </Styled.Root>
    </>
  );
};

export default FeedDetail;
