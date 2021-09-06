import React from 'react';

import useRecentFeedsLoad from 'hooks/queries/feed/useRecentFeedsLoad';
import RegularFeedCard from 'components/RegularFeedCard/RegularFeedCard';
import RegularSkeleton from 'components/RegularSkeleton/RegularSkeleton';
import useSnackbar from 'contexts/snackbar/useSnackbar';
import { FilterType } from 'types';
import Styled from './HomeFeedsContent.styles';

interface Props {
  filter?: FilterType;
  feedsCountToShow: number;
}

const HomeFeedsContent = ({ filter, feedsCountToShow }: Props) => {
  const snackbar = useSnackbar();
  const { data: recentFeeds, isLoading } = useRecentFeedsLoad({
    filter,
    errorHandler: (error) => snackbar.addSnackbar('error', error.message),
    suspense: false,
  });

  return (
    <Styled.Root>
      {isLoading
        ? Array.from({ length: 4 }, (_, idx) => <RegularSkeleton key={idx} />)
        : recentFeeds
            .slice(0, feedsCountToShow)
            .map((feed) => <RegularFeedCard key={feed.id} feed={feed} />)}
    </Styled.Root>
  );
};

export default HomeFeedsContent;
