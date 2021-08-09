import React, { useEffect, useState } from 'react';
import { useHistory } from 'react-router-dom';

import { TechChips } from 'components/SearchBar/SearchBar.styles';
import TechTagProvider from 'context/techTag/TechTagProvider';
import useSnackBar from 'context/snackBar/useSnackBar';
import ROUTE from 'constants/routes';
import useTechs from 'hooks/queries/useTechs';
import SearchIcon from 'assets/search.svg';
import Styled, { TechInput } from './SearchResultHeader.styles';
import { Tech } from 'types';

interface Props {
  searchParams: string;
  query: string;
  setQuery: React.Dispatch<React.SetStateAction<string>>;
  techs: string;
  setTechs: React.Dispatch<React.SetStateAction<string>>;
}

const SearchResultHeader = ({ searchParams, query, setQuery, techs, setTechs }: Props) => {
  const history = useHistory();

  const [queryValue, setQueryValue] = useState('');

  const snackbar = useSnackBar();
  // TODO: 네이밍 고려하기
  const { data: techsData } = useTechs({
    techs,
    errorHandler: (error) => snackbar.addSnackBar('error', error.message),
  });

  const searchByQuery = (event: React.FormEvent) => {
    event.preventDefault();

    const queryParams = new URLSearchParams({
      query: queryValue,
      techs: '',
    });

    history.push({
      pathname: ROUTE.SEARCH,
      search: '?' + queryParams,
    });
  };

  const searchByTechs = (techs: Tech[]) => {
    const queryParams = new URLSearchParams({
      query: '',
      techs: techs.map((tech) => tech.text).join(','),
    });
    history.push({
      pathname: ROUTE.SEARCH,
      search: '?' + queryParams,
    });
  };

  useEffect(() => {
    const params = new URLSearchParams(searchParams);

    setQuery(params.get('query'));
    setQueryValue(params.get('query'));
    setTechs(params.get('techs'));
  }, [searchParams]);

  return (
    <>
      {query && (
        <Styled.SearchBarContainer onSubmit={searchByQuery}>
          <Styled.Input
            value={queryValue}
            onChange={(event) => setQueryValue(event.target.value)}
          />
          <Styled.Button>
            <SearchIcon width="32px" />
          </Styled.Button>
        </Styled.SearchBarContainer>
      )}
      {techs && (
        <TechTagProvider initialTechs={techsData}>
          <Styled.TechTagContainer>
            <TechChips />
            <Styled.TechInputWrapper>
              <TechInput
                onUpdateTechs={searchByTechs}
                placeholder="원하는 기술스택을 추가해주세요"
              />
            </Styled.TechInputWrapper>
          </Styled.TechTagContainer>
        </TechTagProvider>
      )}
    </>
  );
};

export default SearchResultHeader;
