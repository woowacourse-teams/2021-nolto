import React, { useEffect, useState } from 'react';
import { useHistory, useLocation } from 'react-router';

import { FlexContainer } from 'commonStyles';
import RegularFeedCard from 'components/RegularFeedCard/RegularFeedCard';
import StepChip from 'components/StepChip/StepChip';
import HighLightedText from 'components/@common/HighlightedText/HighlightedText';
import RegularSkeleton from 'components/RegularSkeleton/RegularSkeleton';
import Toggle from 'components/@common/Toggle/Toggle';
import useSnackbar from 'contexts/snackbar/useSnackbar';
import useRecentFeedsLoad from 'hooks/queries/feed/useRecentFeedsLoad';
import useIntersectionObserver from 'hooks/@common/useIntersectionObserver';
import { FONT_SIZE } from 'constants/styles';
import { RECENT_FEEDS_PER_PAGE } from 'constants/common';
import { isFeedStep } from 'utils/typeGuard';
import { FeedStep } from 'types';
import Styled from './RecentFeedsContent.styles';

const RecentFeedsContent = () => {
  const location = useLocation();
  const history = useHistory();
  const urlSearchParam = new URLSearchParams(location.search);
  const defaultStep = urlSearchParam.get('step');
  const defaultHelp = Boolean(urlSearchParam.get('help'));

  const [step, setStep] = useState<FeedStep>(isFeedStep(defaultStep) ? defaultStep : null);
  const [help, setHelp] = useState(defaultHelp);

  const snackbar = useSnackbar();

  const { data, hasNextPage, fetchNextPage, isFetching } = useRecentFeedsLoad({
    step,
    help,
    countPerPage: RECENT_FEEDS_PER_PAGE,
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

  useEffect(() => {
    history.replace(
      location.pathname +
        '?' +
        new URLSearchParams({
          step: step || '',
          help: help ? 'true' : '',
        }),
    );
  }, [step, help]);

  return (
    <Styled.Root>
      <FlexContainer flexDirection="column" alignItems="center" gap="1.5rem">
        <h2>
          <HighLightedText fontSize={FONT_SIZE.X_LARGE}>🌱 최신 토이 프로젝트</HighLightedText>
        </h2>
        <Styled.StepChipsContainer>
          <Styled.Button type="button" onClick={() => toggleLevel(FeedStep.PROGRESS)}>
            <StepChip step={FeedStep.PROGRESS} selected={step === FeedStep.PROGRESS} />
          </Styled.Button>
          <Styled.Button type="button" onClick={() => toggleLevel(FeedStep.COMPLETE)}>
            <StepChip step={FeedStep.COMPLETE} selected={step === FeedStep.COMPLETE} />
          </Styled.Button>
          <Toggle
            labelText="🚨도움요청"
            checked={help}
            onChange={() => setHelp(!help)}
            fontSize="14px"
          />
        </Styled.StepChipsContainer>
      </FlexContainer>
      <Styled.RecentFeedsContainer>
        {data?.pages.map((page) =>
          page.feeds.map((feed) => <RegularFeedCard feed={feed} key={feed.id} />),
        )}
        {isFetching &&
          Array.from({ length: RECENT_FEEDS_PER_PAGE }, (_, idx) => <RegularSkeleton key={idx} />)}
      </Styled.RecentFeedsContainer>
      <Styled.MoreHiddenElement ref={targetElement} />
    </Styled.Root>
  );
};

export default RecentFeedsContent;
