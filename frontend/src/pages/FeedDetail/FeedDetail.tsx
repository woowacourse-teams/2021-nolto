import React from 'react';
import { useParams } from 'react-router-dom';

import FeedDetailContent from 'pages/FeedDetail/FeedDetailContent/FeedDetailContent';
import AsyncBoundary from 'components/AsyncBoundary';
import ErrorFallback from 'components/ErrorFallback/ErrorFallback';
import BaseLayout from 'components/BaseLayout/BaseLayout';
import { DefaultPageRoot } from 'commonStyles';

const FeedDetail = () => {
  const params = useParams<{ id: string }>();
  const feedId = Number(params.id);

  return (
    <BaseLayout>
      <DefaultPageRoot>
        <AsyncBoundary
          rejectedFallback={
            <ErrorFallback message="데이터를 불러올 수 없습니다." queryKey="feedDetail" />
          }
        >
          <FeedDetailContent feedId={feedId} />
        </AsyncBoundary>
      </DefaultPageRoot>
    </BaseLayout>
  );
};

export default FeedDetail;
