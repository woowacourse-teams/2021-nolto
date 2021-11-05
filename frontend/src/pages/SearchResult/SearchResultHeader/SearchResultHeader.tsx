import React, { useEffect, useState } from 'react';
import { useHistory } from 'react-router-dom';

import TechTagProvider from 'contexts/techTag/TechTagProvider';
import useSnackbar from 'contexts/snackbar/useSnackbar';
import TechChips from 'contexts/techTag/chip/TechChips';
import TechInput from 'contexts/techTag/input/TechInput';
import { PALETTE } from 'constants/palette';
import ROUTE from 'constants/routes';
import useTechsLoad from 'hooks/queries/useTechsLoad';
import SearchIcon from 'assets/search.svg';
import { Tech } from 'types';
import Styled from './SearchResultHeader.styles';

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

  const snackbar = useSnackbar();

  const { data: techsData } = useTechsLoad({
    techs,
    errorHandler: (error) => snackbar.addSnackbar('error', error.message),
  });

  const searchByQuery = (event: React.FormEvent) => {
    event.preventDefault();

    if (queryValue === '') return;

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
    if (techs.length === 0) return;

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
        <Styled.SearchbarContainer onSubmit={searchByQuery}>
          <Styled.Input
            value={queryValue}
            onChange={(event) => setQueryValue(event.target.value)}
          />
          <Styled.Button>
            <SearchIcon width="32px" fill={PALETTE.PRIMARY_400} />
          </Styled.Button>
        </Styled.SearchbarContainer>
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
