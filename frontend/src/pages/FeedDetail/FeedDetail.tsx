import React from 'react';
import { useParams } from 'react-router-dom';
import { Helmet } from 'react-helmet-async';

import FeedDetailContent from 'pages/FeedDetail/FeedDetailContent/FeedDetailContent';
import AsyncBoundary from 'components/AsyncBoundary';
import ErrorFallback from 'components/ErrorFallback/ErrorFallback';
import BaseLayout from 'components/BaseLayout/BaseLayout';
import { ERROR_MSG } from 'constants/message';
import QUERY_KEYS from 'constants/queryKeys';
import { DefaultPageRoot } from 'commonStyles';

const FeedDetail = () => {
  const params = useParams<{ id: string }>();
  const feedId = Number(params.id);

  return (
    <BaseLayout>
      <Helmet>
        <title>놀토: 토이프로젝트 피드</title>
        <link rel="canonical" href="https://www.nolto.app/feeds" />
      </Helmet>
      <DefaultPageRoot>
        <AsyncBoundary
          rejectedFallback={
            <ErrorFallback message={ERROR_MSG.LOAD_DATA} queryKey={QUERY_KEYS.FEED_DETAIL} />
          }
        >
          <FeedDetailContent feedId={feedId} />
        </AsyncBoundary>
      </DefaultPageRoot>
    </BaseLayout>
  );
};

export default FeedDetail;
