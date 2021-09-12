import React from 'react';

import useRecentFeedsLoad from 'hooks/queries/feed/useRecentFeedsLoad';
import RegularFeedCard from 'components/RegularFeedCard/RegularFeedCard';
import RegularSkeleton from 'components/RegularSkeleton/RegularSkeleton';
import useSnackbar from 'contexts/snackbar/useSnackbar';
import { FeedStep } from 'types';
import Styled from './HomeFeedsContent.styles';

interface Props {
  step?: FeedStep;
  feedsCountToShow: number;
}

const HomeFeedsContent = ({ step, feedsCountToShow }: Props) => {
  const snackbar = useSnackbar();
  const { data, isFetching } = useRecentFeedsLoad({
    step,
    errorHandler: (error) => snackbar.addSnackbar('error', error.message),
    suspense: false,
  });

  return (
    <Styled.Root>
      {data?.pages.map((page) =>
        page.feeds
          .slice(0, feedsCountToShow)
          .map((feed) => <RegularFeedCard key={feed.id} feed={feed} />),
      )}
      {isFetching && Array.from({ length: 4 }, (_, idx) => <RegularSkeleton key={idx} />)}
    </Styled.Root>
  );
};

export default HomeFeedsContent;
