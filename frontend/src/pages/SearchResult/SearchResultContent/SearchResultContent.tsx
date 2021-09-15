import React from 'react';

import RegularFeedCard from 'components/RegularFeedCard/RegularFeedCard';
import RegularSkeleton from 'components/RegularSkeleton/RegularSkeleton';
import useSnackbar from 'contexts/snackbar/useSnackbar';
import useIntersectionObserver from 'hooks/@common/useIntersectionObserver';
import useSearch from 'hooks/queries/useSearch';
import { FeedStep, SearchParams } from 'types';
import Styled from './SearchResultContent.styles';

interface Props {
  searchParams: SearchParams;
  step: FeedStep;
  help: boolean;
}

const FEEDS_PER_PAGE = 20;

const SearchResultContent = ({ searchParams, step, help }: Props) => {
  const snackbar = useSnackbar();

  const { data, hasNextPage, fetchNextPage, isFetching } = useSearch({
    step,
    help,
    searchParams,
    countPerPage: FEEDS_PER_PAGE,
    errorHandler: (error) => snackbar.addSnackbar('error', error.message),
    suspense: false,
  });

  const loadMoreFeeds = () => {
    if (hasNextPage) {
      fetchNextPage();
    }
  };

  const targetElement = useIntersectionObserver(loadMoreFeeds);

  return (
    <Styled.Root>
      <Styled.RecentFeedsContainer>
        {data?.pages.map((page) => page.feeds.map((feed) => <RegularFeedCard feed={feed} />))}
        {isFetching &&
          Array.from({ length: FEEDS_PER_PAGE }, (_, idx) => <RegularSkeleton key={idx} />)}
      </Styled.RecentFeedsContainer>
      <Styled.MoreHiddenElement ref={targetElement} />
    </Styled.Root>
  );
};

export default SearchResultContent;
