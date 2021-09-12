import React, { useState } from 'react';
import { useLocation } from 'react-router';

import { FlexContainer } from 'commonStyles';
import RegularFeedCard from 'components/RegularFeedCard/RegularFeedCard';
import StepChip from 'components/StepChip/StepChip';
import { HighLightedText } from 'components/TeamMember/TeamMember.styles';
import RegularSkeleton from 'components/RegularSkeleton/RegularSkeleton';
import Toggle from 'components/@common/Toggle/Toggle';
import useSnackbar from 'contexts/snackbar/useSnackbar';
import useRecentFeedsLoad from 'hooks/queries/feed/useRecentFeedsLoad';
import useIntersectionObserver from 'hooks/@common/useIntersectionObserver';
import { FONT_SIZE } from 'constants/styles';
import { FeedStep } from 'types';
import Styled from './RecentFeedsContent.styles';

const FEEDS_PER_PAGE = 20;

const RecentFeedsContent = () => {
  const location = useLocation<{ step: FeedStep }>();
  const defaultStep = location.state?.step;

  const [step, setStep] = useState<FeedStep>(defaultStep);
  const [help, setHelp] = useState(false);

  const snackbar = useSnackbar();

  const { data, hasNextPage, fetchNextPage, isFetching } = useRecentFeedsLoad({
    step,
    help,
    countPerPage: FEEDS_PER_PAGE,
    errorHandler: (error) => snackbar.addSnackbar('error', error.message),
    suspense: false,
  });

  const toggleLevel = (feedStep: FeedStep) => {
    if (step === feedStep) setStep(null);
    else setStep(feedStep);
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
          <Styled.Button type="button" onClick={() => toggleLevel(FeedStep.PROGRESS)}>
            <StepChip step={FeedStep.PROGRESS} selected={step === FeedStep.PROGRESS} />
          </Styled.Button>
          <Styled.Button type="button" onClick={() => toggleLevel(FeedStep.COMPLETE)}>
            <StepChip step={FeedStep.COMPLETE} selected={step === FeedStep.COMPLETE} />
          </Styled.Button>
          <Toggle labelText="🚨도움요청" onChange={() => setHelp(!help)} fontSize="14px" />
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
