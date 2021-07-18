import React, { useState, useRef, useEffect } from 'react';
import { Link } from 'react-router-dom';

import CroppedEllipse from 'components/CroppedEllipse/CroppedEllipse';
import RegularCard from 'components/RegularCard/RegularCard';
import StretchCard from 'components/StretchCard/StretchCard';
import LevelLinkButton from 'components/LevelLinkButton/LevelLinkButton';
import Header from 'components/Header/Header';
import useHotFeeds from 'hooks/queries/useHotFeeds';
import useOnScreen from 'hooks/@common/useOnScreen';
import useRecentFeeds from 'hooks/queries/useRecentFeeds';
import ROUTE from 'constants/routes';
import Styled, { CarouselArrowButton, ScrollUpButton, Searchbar, MoreButton } from './Home.styles';
import MoreArrow from 'assets/moreArrow.svg';
import { ButtonStyle } from 'types';

const tags = ['JavaScript', 'Java', 'React.js', 'Spring'];

const Home = () => {
  const { data: hotFeeds } = useHotFeeds();
  const { data: recentFeeds } = useRecentFeeds();

  const [isHeaderFolded, setHeaderFolded] = useState(true);
  const [hotToyCardIdx, setHotToyCardIdx] = useState(3);

  const ellipseRef = useRef();
  const isEllipseVisible = useOnScreen(ellipseRef);

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

  return (
    <>
      <Header isFolded={isHeaderFolded} />
      <Styled.Root>
        <Styled.EllipseWrapper ref={ellipseRef}>
          <CroppedEllipse />
        </Styled.EllipseWrapper>
        <Styled.SearchContainer>
          <Styled.SearchTitle>Search for Ideas?</Styled.SearchTitle>
          <Searchbar />
          <Styled.TagsContainer>
            {tags.map((tag) => (
              <Styled.TagButton buttonStyle={ButtonStyle.SOLID} reverse={true} key={tag}>
                {tag}
              </Styled.TagButton>
            ))}
          </Styled.TagsContainer>
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
                  <Styled.HotToyCardWrapper key={feed.id} offset={idx + 1}>
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
            <Styled.LevelButtonsContainer>
              <LevelLinkButton.Progress />
              <LevelLinkButton.Complete />
              <LevelLinkButton.SOS />
            </Styled.LevelButtonsContainer>
            <Styled.RecentToyCardsContainer>
              {recentFeeds &&
                recentFeeds.slice(0, 4).map((feed) => (
                  <li key={feed.id}>
                    <Link to={`${ROUTE.FEEDS}/${feed.id}`}>
                      <Styled.VerticalAvatar user={feed.author} />
                      <StretchCard feed={feed} />
                    </Link>
                  </li>
                ))}
            </Styled.RecentToyCardsContainer>
            <MoreButton to={ROUTE.FEEDS}>
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
