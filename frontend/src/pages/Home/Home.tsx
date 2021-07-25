import React, { useState, useRef, useEffect } from 'react';
import { Link, useHistory } from 'react-router-dom';

import AsyncBoundary from 'components/AsyncBoundary';
import CroppedEllipse from 'components/CroppedEllipse/CroppedEllipse';
import RegularCard from 'components/RegularCard/RegularCard';
import Header from 'components/Header/Header';
import RecentFeedsContent from 'components/RecentFeedsContent/RecentFeedsContent';
import useHotFeeds from 'hooks/queries/useHotFeeds';
import useOnScreen from 'hooks/@common/useOnScreen';
import ROUTE from 'constants/routes';
import Styled, { CarouselArrowButton, ScrollUpButton, SearchBar, MoreButton } from './Home.styles';
import MoreArrow from 'assets/moreArrow.svg';
import { Tech } from 'types';

const tags: Tech[] = [
  {
    id: 25,
    text: 'ReactJS',
  },
  {
    id: 882,
    text: 'Java',
  },
  {
    id: 655,
    text: 'JavaScript',
  },
  {
    id: 67,
    text: 'Spring',
  },
];

const Home = () => {
  const history = useHistory();

  const { data: hotFeeds } = useHotFeeds({
    onError: () => alert('임시 alert'),
  });

  const [isHeaderFolded, setHeaderFolded] = useState(true);
  const [hotToyCardIdx, setHotToyCardIdx] = useState(3);

  const ellipseRef = useRef();
  const isEllipseVisible = useOnScreen(ellipseRef);

  const RECENT_FEED_LENGTH = 4;

  useEffect(() => {
    if (isEllipseVisible) {
      setHeaderFolded(true);
    } else {
      setHeaderFolded(false);
    }
  }, [isEllipseVisible]);

  const scrollTop = () => {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const showPreviousCards = () => {
    if (hotToyCardIdx > 1) setHotToyCardIdx(hotToyCardIdx - 1);
  };

  const showFollowingCards = () => {
    if (hotToyCardIdx < hotFeeds?.length) setHotToyCardIdx(hotToyCardIdx + 1);
  };

  const searchByTrend = (tech: Tech) => {
    const queryParams = new URLSearchParams({
      query: '',
      techs: tech.text,
    });

    history.push({
      pathname: ROUTE.SEARCH,
      search: '?' + queryParams,
    });
  };

  return (
    <>
      <Header isFolded={isHeaderFolded} />
      <Styled.Root>
        <Styled.EllipseWrapper ref={ellipseRef}>
          <CroppedEllipse />
        </Styled.EllipseWrapper>
        <Styled.SearchContainer>
          <Styled.SearchTitle>Search for Ideas?</Styled.SearchTitle>
          <SearchBar selectable />
          <Styled.TrendContainer>
            <span className="trends">💎 Trends</span>
            {tags.map((tag) => (
              <Styled.TrendTag key={tag.id} onClick={() => searchByTrend(tag)}>
                {tag.text}
              </Styled.TrendTag>
            ))}
          </Styled.TrendContainer>
        </Styled.SearchContainer>

        <Styled.ContentArea>
          <Styled.SectionTitle fontSize="1.75rem">Hot Toys</Styled.SectionTitle>
          <Styled.HotToysContainer>
            <CarouselArrowButton onClick={showPreviousCards}>
              <Styled.CarouselLeft width="32px" />
            </CarouselArrowButton>
            <Styled.HotToyCardsContainer position={hotToyCardIdx}>
              {hotFeeds &&
                hotFeeds.map((feed, idx) => (
                  <Styled.HotToyCardWrapper key={feed.id} offset={idx + 1} position={hotToyCardIdx}>
                    <Link to={`${ROUTE.FEEDS}/${feed.id}`}>
                      <Styled.VerticalAvatar user={feed.author} />
                      <RegularCard feed={feed} />
                    </Link>
                  </Styled.HotToyCardWrapper>
                ))}
            </Styled.HotToyCardsContainer>

            <CarouselArrowButton onClick={showFollowingCards}>
              <Styled.CarouselRight width="32px" />
            </CarouselArrowButton>
          </Styled.HotToysContainer>

          <Styled.SectionTitle fontSize="1.75rem">Recent Toys</Styled.SectionTitle>
          <Styled.RecentToysContainer>
            <AsyncBoundary rejectedFallback={<h1>임시 에러 페이지</h1>}>
              <RecentFeedsContent limit={RECENT_FEED_LENGTH} />
            </AsyncBoundary>
            <MoreButton to={ROUTE.RECENT}>
              MORE&nbsp;
              <MoreArrow width="10px" />
            </MoreButton>
          </Styled.RecentToysContainer>
        </Styled.ContentArea>
        <ScrollUpButton onClick={scrollTop}>
          <Styled.ArrowUp width="10px" />
        </ScrollUpButton>
      </Styled.Root>
    </>
  );
};

export default Home;
