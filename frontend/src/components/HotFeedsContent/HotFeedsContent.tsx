import React, { useState } from 'react';

import useHotFeedsLoad from 'hooks/queries/feed/useHotFeedsLoad';
import useSnackbar from 'contexts/snackbar/useSnackbar';
import LargeFeedCard from 'components/LargeFeedCard/LargeFeedCard';
import LargeSkeleton from 'components/LargeSkeleton/LargeSkeleton';
import { FlexContainer } from 'commonStyles';
import Styled, { CarouselArrowButton } from './HotFeedsContent.styles';
import IconButton from 'components/@common/IconButton/IconButton';

const HotFeedsContent = () => {
  const [hotToyCardIdx, setHotToyCardIdx] = useState(0);

  const snackbar = useSnackbar();

  const { data: hotFeeds, isLoading } = useHotFeedsLoad({
    errorHandler: (error) => {
      snackbar.addSnackbar('error', error.message);
    },
    suspense: false,
  });

  const showPreviousCards = () => {
    if (hotToyCardIdx > 0) setHotToyCardIdx(hotToyCardIdx - 1);
  };

  const showFollowingCards = () => {
    if (hotToyCardIdx < hotFeeds?.length - 1) setHotToyCardIdx(hotToyCardIdx + 1);
  };

  return (
    <Styled.Root>
      <Styled.HotToyCardsContainer position={hotToyCardIdx}>
        {isLoading
          ? Array.from({ length: 3 }, (_, idx) => (
              <Styled.HotToyCardWrapper
                className="hot-feed"
                key={idx}
                offset={idx + 1}
                position={hotToyCardIdx}
              >
                <LargeSkeleton />
              </Styled.HotToyCardWrapper>
            ))
          : hotFeeds.map((feed, idx) => (
              <Styled.HotToyCardWrapper
                className="hot-feed"
                key={feed.id}
                offset={idx + 1}
                position={hotToyCardIdx}
              >
                <LargeFeedCard feed={feed} />
              </Styled.HotToyCardWrapper>
            ))}
      </Styled.HotToyCardsContainer>
      <Styled.ControlContainer className="control-container">
        <CarouselArrowButton
          size="3rem"
          className="carousel-button"
          onClick={showPreviousCards}
          aria-label="이전 피드 보기"
        >
          <Styled.CarouselLeft width="1.5rem" />
        </CarouselArrowButton>
        <CarouselArrowButton
          size="3rem"
          className="carousel-button"
          onClick={showFollowingCards}
          aria-label="다음 피드 보기"
        >
          <Styled.CarouselRight width="1.5rem" />
        </CarouselArrowButton>
      </Styled.ControlContainer>
      <FlexContainer className="dot-container" gap="0.25rem" justifyContent="center">
        {hotFeeds?.map((_, idx) => (
          <IconButton
            size="0.75rem"
            hasShadow={false}
            key={idx}
            onClick={() => {
              setHotToyCardIdx(idx);
            }}
            aria-label={`${idx}번째 피드로 이동`}
          >
            <Styled.Dot selected={idx === hotToyCardIdx} />
          </IconButton>
        ))}
      </FlexContainer>
    </Styled.Root>
  );
};

export default HotFeedsContent;
