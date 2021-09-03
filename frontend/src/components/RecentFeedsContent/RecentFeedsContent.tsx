import React, { useState } from 'react';

import { FlexContainer } from 'commonStyles';
import RegularFeedCard from 'components/RegularFeedCard/RegularFeedCard';
import StepChip from 'components/StepChip/StepChip';
import { HighLightedText } from 'components/TeamMember/TeamMember.styles';
import useSnackbar from 'contexts/snackbar/useSnackbar';
import useRecentFeedsLoad from 'hooks/queries/feed/useRecentFeedsLoad';
import { FONT_SIZE } from 'constants/styles';
import { FeedStatus, FilterType } from 'types';
import Styled from './RecentFeedsContent.styles';

const RecentFeedsContent = () => {
  const [filter, setFilter] = useState<FilterType>();

  const snackbar = useSnackbar();
  const { data: recentFeeds } = useRecentFeedsLoad({
    filter,
    errorHandler: (error) => snackbar.addSnackbar('error', error.message),
  });

  const toggleLevel = (filterType: FilterType) => {
    if (filter === filterType) setFilter(null);
    else setFilter(filterType);
  };

  return (
    <Styled.Root>
      <FlexContainer flexDirection="column" gap="1rem">
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
        {recentFeeds.map((feed) => (
          <RegularFeedCard feed={feed} />
        ))}
      </Styled.RecentFeedsContainer>
    </Styled.Root>
  );
};

export default RecentFeedsContent;
