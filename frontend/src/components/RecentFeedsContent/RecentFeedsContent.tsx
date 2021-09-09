import React, { useState } from 'react';

import { FlexContainer } from 'commonStyles';
import RegularFeedCard from 'components/RegularFeedCard/RegularFeedCard';
import StepChip from 'components/StepChip/StepChip';
import { HighLightedText } from 'components/TeamMember/TeamMember.styles';
import RegularSkeleton from 'components/RegularSkeleton/RegularSkeleton';
import useSnackbar from 'contexts/snackbar/useSnackbar';
import useRecentFeedsLoad from 'hooks/queries/feed/useRecentFeedsLoad';
import useIntersectionObserver from 'hooks/@common/useIntersectionObserver';
import { FONT_SIZE } from 'constants/styles';
import { FeedStatus, FilterType } from 'types';
import Styled from './RecentFeedsContent.styles';

const FEEDS_PER_PAGE = 20;

const RecentFeedsContent = () => {
  const [filter, setFilter] = useState<FilterType>();

  const snackbar = useSnackbar();

  const { data, hasNextPage, fetchNextPage, isFetching } = useRecentFeedsLoad({
    filter,
    countPerPage: FEEDS_PER_PAGE,
    errorHandler: (error) => snackbar.addSnackbar('error', error.message),
    suspense: false,
  });

  const toggleLevel = (filterType: FilterType) => {
    if (filter === filterType) setFilter(null);
    else setFilter(filterType);
  };

  const loadMoreFeeds = () => {
    if (hasNextPage) {
      fetchNextPage();
    }
  };

  const targetElement = useIntersectionObserver(loadMoreFeeds);

  return (
    <Styled.Root>
      <FlexContainer flexDirection="column" alignItems="center" gap="1.5rem">
        <HighLightedText fontSize={FONT_SIZE.X_LARGE}>Recent Toys</HighLightedText>
        <Styled.StepChipsContainer>
          <Styled.Button type="button" onClick={() => toggleLevel(FilterType.PROGRESS)}>
            <StepChip step={FeedStatus.PROGRESS} selected={filter === FilterType.PROGRESS} />
          </Styled.Button>
          <Styled.Button type="button" onClick={() => toggleLevel(FilterType.COMPLETE)}>
            <StepChip step={FeedStatus.COMPLETE} selected={filter === FilterType.COMPLETE} />
          </Styled.Button>
          <Styled.Button type="button" onClick={() => toggleLevel(FilterType.SOS)}>
            <StepChip step={FeedStatus.SOS} selected={filter === FilterType.SOS} />
          </Styled.Button>
        </Styled.StepChipsContainer>
      </FlexContainer>
      <Styled.RecentFeedsContainer>
        {data?.pages.map((page) => page.feeds.map((feed) => <RegularFeedCard feed={feed} />))}
        {isFetching &&
          Array.from({ length: FEEDS_PER_PAGE }, (_, idx) => <RegularSkeleton key={idx} />)}
      </Styled.RecentFeedsContainer>
      <Styled.MoreHiddenElement ref={targetElement} />
    </Styled.Root>
  );
};

export default RecentFeedsContent;
