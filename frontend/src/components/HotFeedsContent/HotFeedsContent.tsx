import React, { useState } from 'react';
import { Link } from 'react-router-dom';

import useHotFeedsLoad from 'hooks/queries/feed/useHotFeedsLoad';
import LargeFeedCard from 'components/LargeFeedCard/LargeFeedCard';
import ROUTE from 'constants/routes';
import Styled, { CarouselArrowButton } from './HotFeedsContent.styles';
import useSnackbar from 'contexts/snackbar/useSnackbar';

const HotFeedsContent = () => {
  const [hotToyCardIdx, setHotToyCardIdx] = useState(2);

  const snackbar = useSnackbar();

  const { data: hotFeeds } = useHotFeedsLoad({
    errorHandler: (error) => {
      snackbar.addSnackbar('error', error.message);
    },
  });

  const showPreviousCards = () => {
    if (hotToyCardIdx > 2) setHotToyCardIdx(hotToyCardIdx - 1);
  };

  const showFollowingCards = () => {
    if (hotToyCardIdx < hotFeeds?.length) setHotToyCardIdx(hotToyCardIdx + 1);
  };

  //TODO: 지금 hotToy 클릭안됨
  return (
    <Styled.Root>
      <Styled.HotToyCardsContainer position={hotToyCardIdx}>
        {hotFeeds &&
          hotFeeds.map((feed, idx) => (
            <Styled.HotToyCardWrapper key={feed.id} offset={idx + 1} position={hotToyCardIdx}>
              <Link to={`${ROUTE.FEEDS}/${feed.id}`}>
                <Styled.VerticalAvatar user={feed.author} />
                <LargeFeedCard feed={feed} />
              </Link>
            </Styled.HotToyCardWrapper>
          ))}
      </Styled.HotToyCardsContainer>
      <Styled.ControlContainer>
        <CarouselArrowButton className="carousel-button" onClick={showPreviousCards}>
          <Styled.CarouselLeft width="1.5rem" />
        </CarouselArrowButton>
        <CarouselArrowButton className="carousel-button" onClick={showFollowingCards}>
          <Styled.CarouselRight width="1.5rem" />
        </CarouselArrowButton>
      </Styled.ControlContainer>
    </Styled.Root>
  );
};

export default HotFeedsContent;
