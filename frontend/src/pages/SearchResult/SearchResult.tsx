import React, { useState } from 'react';
import { useLocation } from 'react-router-dom';

import Header from 'components/Header/Header';
import LevelButton from 'components/LevelButton/LevelButton';
import AsyncBoundary from 'components/AsyncBoundary';
import SearchResultContent from 'components/SearchResultContent/SearchResultContent';
import SearchResultHeader from 'components/SearchResultHeader/SearchResultHeader';
import Styled from './SearchResult.styles';
import { Tech } from 'types';

interface LocationState {
  query: string;
  techs: Tech[];
}

const SearchResult = () => {
  const location = useLocation<LocationState>();

  const [query, setQuery] = useState('');
  const [techs, setTechs] = useState('');

  return (
    <>
      <Header />
      <Styled.Root>
        <Styled.SectionTitle fontSize="1.75rem">Toys About</Styled.SectionTitle>
        <AsyncBoundary rejectedFallback={<div>게시물 검색에 실패했습니다.</div>}>
          <SearchResultHeader
            searchParams={location.search}
            query={query}
            setQuery={setQuery}
            techs={techs}
            setTechs={setTechs}
          />
        </AsyncBoundary>

        <Styled.RecentToysContainer>
          <Styled.LevelButtonsContainer>
            <LevelButton.Progress />
            <LevelButton.Complete />
            <LevelButton.SOS />
          </Styled.LevelButtonsContainer>

          <AsyncBoundary rejectedFallback={<div>게시물 검색에 실패했습니다.</div>}>
            <SearchResultContent query={query} techs={techs} />
          </AsyncBoundary>
        </Styled.RecentToysContainer>
      </Styled.Root>
    </>
  );
};

export default SearchResult;
