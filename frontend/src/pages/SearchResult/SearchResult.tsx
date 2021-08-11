import React, { useState } from 'react';
import { useLocation } from 'react-router-dom';

import Header from 'components/Header/Header';
import AsyncBoundary from 'components/AsyncBoundary';
import SearchResultContent from 'pages/SearchResult/SearchResultContent/SearchResultContent';
import SearchResultHeader from 'pages/SearchResult/SearchResultHeader/SearchResultHeader';
import StepChip from 'components/StepChip/StepChip';
import Styled from './SearchResult.styles';
import { FeedStatus, FilterType, Tech } from 'types';

interface LocationState {
  query: string;
  techs: Tech[];
}

const SearchResult = () => {
  const location = useLocation<LocationState>();

  const [query, setQuery] = useState('');
  const [techs, setTechs] = useState('');
  const [filter, setFilter] = useState<FilterType>(null);

  const toggleLevel = (filterType: FilterType) => {
    if (filter === filterType) setFilter(null);
    else setFilter(filterType);
  };

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

        <Styled.StepChipsContainer>
          <StepChip
            step={FeedStatus.PROGRESS}
            onSelect={() => toggleLevel(FilterType.PROGRESS)}
            selected={filter === FilterType.PROGRESS}
          />
          <StepChip
            step={FeedStatus.COMPLETE}
            onSelect={() => toggleLevel(FilterType.COMPLETE)}
            selected={filter === FilterType.COMPLETE}
          />
          <StepChip
            step={FeedStatus.SOS}
            onSelect={() => toggleLevel(FilterType.SOS)}
            selected={filter === FilterType.SOS}
          />
        </Styled.StepChipsContainer>

        <Styled.RecentToysContainer>
          <AsyncBoundary rejectedFallback={<div>게시물 검색에 실패했습니다.</div>}>
            <SearchResultContent query={query} techs={techs} filter={filter} />
          </AsyncBoundary>
        </Styled.RecentToysContainer>
      </Styled.Root>
    </>
  );
};

export default SearchResult;
