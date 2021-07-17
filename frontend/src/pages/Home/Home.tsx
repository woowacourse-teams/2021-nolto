import React from 'react';
import { Link } from 'react-router-dom';

import CroppedEllipse from 'components/CroppedEllipse/CroppedEllipse';
import RegularCard from 'components/RegularCard/RegularCard';
import StretchCard from 'components/StretchCard/StretchCard';
import LevelLinkButton from 'components/LevelLinkButton/LevelLinkButton';
import Header from 'components/Header/Header';
import useHotFeeds from 'hooks/queries/useHotFeeds';
import useRecentFeeds from 'hooks/queries/useRecentFeeds';
import ROUTE from 'constants/routes';
import Styled from './Home.styles';
import MoreArrow from 'assets/moreArrow.svg';
import { ButtonStyle } from 'types';

const tags = ['JavaScript', 'Java', 'React.js', 'Spring'];

const Home = () => {
  const { data: hotFeeds } = useHotFeeds();
  const { data: recentFeeds } = useRecentFeeds();

  return (
    <>
      <Header isFolded={true} />
      <Styled.Root>
        <Styled.EllipseWrapper>
          <CroppedEllipse />
        </Styled.EllipseWrapper>
        <Styled.SearchContainer>
          <Styled.SearchTitle>Search for Ideas?</Styled.SearchTitle>
          <Styled.MainSearchBar />
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
            <Styled.CarouselLeft width="24" />
            <Styled.HotToyCardsContainer>
              {hotFeeds &&
                hotFeeds.map((feed) => (
                  <li key={feed.id}>
                    <Link to={`${ROUTE.FEEDS}/${feed.id}`}>
                      <Styled.VerticalAvatar user={feed.author} />
                      <RegularCard feed={feed} />
                    </Link>
                  </li>
                ))}
            </Styled.HotToyCardsContainer>
            <Styled.CarouselRight width="24" />
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
                recentFeeds.map((feed) => (
                  <li key={feed.id}>
                    <Link to={`${ROUTE.FEEDS}/${feed.id}`}>
                      <Styled.VerticalAvatar user={feed.author} />
                      <StretchCard feed={feed} />
                    </Link>
                  </li>
                ))}
            </Styled.RecentToyCardsContainer>
            <Styled.MoreButton>
              MORE&nbsp;
              <MoreArrow width="10" />
            </Styled.MoreButton>
          </Styled.RecentToysContainer>
        </Styled.ContentArea>
      </Styled.Root>
    </>
  );
};

export default Home;
