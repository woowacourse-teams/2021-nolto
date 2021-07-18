import React from 'react';
import { Link } from 'react-router-dom';

import useRecentFeeds from 'hooks/queries/useRecentFeeds';
import Header from 'components/Header/Header';
import LevelLinkButton from 'components/LevelLinkButton/LevelLinkButton';
import StretchCard from 'components/StretchCard/StretchCard';
import ROUTE from 'constants/routes';
import Styled from './RecentFeeds.styles';

const RecentFeeds = () => {
  const { data: recentFeeds } = useRecentFeeds();

  return (
    <>
      <Header />
      <Styled.Root>
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
        </Styled.RecentToysContainer>
      </Styled.Root>
    </>
  );
};

export default RecentFeeds;
