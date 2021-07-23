import React from 'react';
import { useParams, Link, useLocation } from 'react-router-dom';

import Header from 'components/Header/Header';
import LevelLinkButton from 'components/LevelLinkButton/LevelLinkButton';
import StretchCard from 'components/StretchCard/StretchCard';
import useSearch from 'hooks/queries/useSearch';
import Styled from './SearchResult.styles';
import ROUTE from 'constants/routes';

// interface Props {
//   query: string;
//   techs: string;
// }

const SearchResult = () => {
  const { query, techs } = useParams<{ query: string; techs: string }>();
  // const location = useLocation();
  const { data: feeds } = useSearch({ query, techs });

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
            {feeds?.map((feed) => (
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

export default SearchResult;
