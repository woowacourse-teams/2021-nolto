import React, { InputHTMLAttributes, useEffect, useState } from 'react';
import { useHistory } from 'react-router-dom';

import SearchIcon from 'assets/search.svg';
import TechTagProvider from 'contexts/techTag/TechTagProvider';
import TechChips from 'contexts/techTag/chip/TechChips';
import ROUTE from 'constants/routes';
import { PALETTE } from 'constants/palette';
import SearchOption from 'components/SearchOption/SearchOption';
import { Tech, SearchType } from 'types';
import Styled, { TechInput } from './Searchbar.styles';

interface Props extends InputHTMLAttributes<HTMLInputElement> {
  className?: string;
  selectable?: boolean;
}

const Searchbar = ({ className, selectable = false, ...options }: Props) => {
  const history = useHistory();

  const [query, setQuery] = useState<string>('');
  const [techs, setTechs] = useState<Tech[]>([]);
  const [searchType, setSearchType] = useState<SearchType>(SearchType.CONTENT);

  const search = (event: React.FormEvent) => {
    event.preventDefault();

    if (
      (searchType === SearchType.CONTENT && query === '') ||
      (searchType === SearchType.TECH && techs.length === 0)
    ) {
      history.push(ROUTE.RECENT);

      return;
    }

    const queryParams = new URLSearchParams({
      query,
      techs: techs.map((tech) => tech.text).join(','),
    });

    history.push({
      pathname: ROUTE.SEARCH,
      search: '?' + queryParams,
    });
  };

  useEffect(() => {
    setQuery('');
    setTechs([]);
  }, [searchType]);

  const selectedTags: React.ReactNode = (
    <Styled.SelectedChips>
      <span>Selected:</span>
      <TechChips reverse={true} />
    </Styled.SelectedChips>
  );

  return (
    <TechTagProvider>
      {searchType === SearchType.TECH && selectedTags}
      <Styled.Root className={className} selectable={selectable} onSubmit={search}>
        {selectable && <SearchOption searchType={searchType} setSearchType={setSearchType} />}
        {searchType === SearchType.CONTENT && (
          <Styled.Input
            onChange={(event) => setQuery(event.target.value)}
            {...options}
            aria-label="??????/???????????? ??????"
          />
        )}
        {searchType === SearchType.TECH && (
          <TechInput
            onUpdateTechs={(techs: Tech[]) => setTechs(techs)}
            placeholder="???????????? ?????? ??? ?????? ?????? ???????????? ???????????????"
            aria-label="?????????????????? ??????"
          />
        )}
        <Styled.Button aria-label="??????">
          <SearchIcon width="32px" fill={PALETTE.PRIMARY_400} />
        </Styled.Button>
      </Styled.Root>
    </TechTagProvider>
  );
};

export default Searchbar;
