import React from 'react';
import { Link, useParams } from 'react-router-dom';

import useRecentFeeds from 'hooks/queries/useRecentFeeds';
import Header from 'components/Header/Header';
import LevelLinkButton from 'components/LevelLinkButton/LevelLinkButton';
import StretchCard from 'components/StretchCard/StretchCard';
import ROUTE from 'constants/routes';
import Styled from './RecentFeeds.styles';
import { FilterType } from 'types';

const RecentFeeds = () => {
  const { filterType } = useParams<{ filterType?: FilterType }>();
  const { data: recentFeeds } = useRecentFeeds(filterType);

  return (
    <>
      <Header />
      <Styled.Root>
        <Link to={ROUTE.RECENT}>
          <Styled.SectionTitle fontSize="1.75rem">Recent Toys</Styled.SectionTitle>
        </Link>
        <Styled.RecentToysContainer>
          <Styled.LevelButtonsContainer>
            <LevelLinkButton.Progress
              path={`${ROUTE.RECENT}/${FilterType.PROGRESS}`}
              selected={filterType === FilterType.PROGRESS}
            />
            <LevelLinkButton.Complete
              path={`${ROUTE.RECENT}/${FilterType.COMPLETE}`}
              selected={filterType === FilterType.COMPLETE}
            />
            <LevelLinkButton.SOS
              path={`${ROUTE.RECENT}/${FilterType.SOS}`}
              selected={filterType === FilterType.SOS}
            />
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
        </Styled.RecentToysContainer>
      </Styled.Root>
    </>
  );
};

export default RecentFeeds;
