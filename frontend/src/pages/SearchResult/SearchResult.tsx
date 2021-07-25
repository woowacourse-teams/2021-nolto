import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';

import Header from 'components/Header/Header';
import LevelButton from 'components/LevelButton/LevelButton';
import AsyncBoundary from 'components/AsyncBoundary';
import SearchResultContent from 'components/SearchResultContent/SearchResultContent';
import { TechChips } from 'components/SearchBar/SearchBar.styles';
import TechTagProvider from 'context/techTag/TechTagProvider';
import SearchIcon from 'assets/search.svg';
import Styled, { TechInput } from './SearchResult.styles';
import { Tech } from 'types';

interface LocationState {
  query: string;
  techs: Tech[];
}

const SearchResult = () => {
  const location = useLocation<LocationState>();
  const params = new URLSearchParams(location.search);

  const [queryParam, setQueryParam] = useState(params.get('query'));
  const [query, setQuery] = useState(location.state?.query);
  const [techsParm, setTechsParm] = useState(params.get('techs'));
  const [techs, setTechs] = useState<Tech[]>(location.state?.techs);

  const research = (event: React.FormEvent) => {
    event.preventDefault();
    setQueryParam(query);
  };

  useEffect(() => {
    if (techs) setTechsParm(techs.map((tech) => tech.text).join(','));
  }, [techs]);

  return (
    <>
      <Header />
      <Styled.Root>
        <Styled.SectionTitle fontSize="1.75rem">Toys About</Styled.SectionTitle>
        {queryParam && (
          <Styled.SearchBarContainer onSubmit={research}>
            <Styled.Input value={query} onChange={(event) => setQuery(event.target.value)} />
            <Styled.Button>
              <SearchIcon width="32px" />
            </Styled.Button>
          </Styled.SearchBarContainer>
        )}
        {techsParm && (
          <TechTagProvider initialTechs={techs}>
            <Styled.TechTagContainer>
              <TechChips />
              <Styled.TechInputWrapper>
                <TechInput
                  onUpdateTechs={(techs: Tech[]) => setTechs(techs)}
                  placeholder="원하는 기술스택을 추가해주세요"
                />
              </Styled.TechInputWrapper>
            </Styled.TechTagContainer>
          </TechTagProvider>
        )}
        <Styled.RecentToysContainer>
          <Styled.LevelButtonsContainer>
            <LevelButton.Progress />
            <LevelButton.Complete />
            <LevelButton.SOS />
          </Styled.LevelButtonsContainer>
          <AsyncBoundary rejectedFallback={<div>존재하지 않는 게시물입니닷 (임시)</div>}>
            <SearchResultContent query={queryParam} techs={techsParm} />
          </AsyncBoundary>
        </Styled.RecentToysContainer>
      </Styled.Root>
    </>
  );
};

export default SearchResult;
