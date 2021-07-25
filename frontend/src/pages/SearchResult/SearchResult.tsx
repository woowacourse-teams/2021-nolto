import React from 'react';
import { useLocation } from 'react-router-dom';

import Header from 'components/Header/Header';
import LevelLinkButton from 'components/LevelLinkButton/LevelLinkButton';
import AsyncBoundary from 'components/AsyncBoundary';
import SearchResultContent from 'components/SearchResultContent/SearchResultContent';
import Styled from './SearchResult.styles';

const SearchResult = () => {
  const location = useLocation();
  const params = new URLSearchParams(location.search);

  const query = params.get('query');
  const techs = params.get('techs');

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
          <AsyncBoundary rejectedFallback={<div>존재하지 않는 게시물입니닷 (임시)</div>}>
            <SearchResultContent query={query} techs={techs} />
          </AsyncBoundary>
        </Styled.RecentToysContainer>
      </Styled.Root>
    </>
  );
};

export default SearchResult;
