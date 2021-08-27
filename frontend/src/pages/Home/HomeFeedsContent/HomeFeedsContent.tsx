import RegularCard from 'components/RegularCard/RegularCard';
import React from 'react';

import useRecentFeedsLoad from 'hooks/queries/feed/useRecentFeedsLoad';
import { FilterType } from 'types';
import useSnackbar from 'contexts/snackbar/useSnackbar';
import Styled from './HomeFeedsContent.styles';

interface Props {
  filter?: FilterType;
  feedsCountToShow?: number;
}

const HomeFeedsContent = ({ filter, feedsCountToShow = 5 }: Props) => {
  const snackbar = useSnackbar();
  const { data: recentFeeds } = useRecentFeedsLoad({
    filter,
    errorHandler: (error) => snackbar.addSnackbar('error', error.message),
  });

  return (
    <Styled.Root>
      {recentFeeds.slice(0, feedsCountToShow).map((feed) => (
        <RegularCard feed={feed} />
      ))}
    </Styled.Root>
  );
};

export default HomeFeedsContent;
