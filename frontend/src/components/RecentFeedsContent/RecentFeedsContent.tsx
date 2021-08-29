import { FlexContainer } from 'commonStyles';
import RegularCard from 'components/RegularCard/RegularCard';
import StepChip from 'components/StepChip/StepChip';
import { HighLightedText } from 'components/TeamMember/TeamMember.styles';
import useSnackbar from 'contexts/snackbar/useSnackbar';
import useRecentFeedsLoad from 'hooks/queries/feed/useRecentFeedsLoad';
import React, { useState } from 'react';
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
        <HighLightedText fontSize="1.75rem">Recent Toys</HighLightedText>
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
          <RegularCard feed={feed} />
        ))}
      </Styled.RecentFeedsContainer>
    </Styled.Root>
  );
};

export default RecentFeedsContent;
