import React, { InputHTMLAttributes, useEffect, useState } from 'react';
import { useHistory } from 'react-router-dom';

import SearchIcon from 'assets/search.svg';
import TechTagProvider from 'context/techTag/TechTagProvider';
import ROUTE from 'constants/routes';
import SearchOption from 'components/SearchOption/SearchOption';
import Styled, { TechChips, TechInput } from './SearchBar.styles';
import { Tech, SearchType } from 'types';

interface Props extends InputHTMLAttributes<HTMLInputElement> {
  className?: string;
  selectable?: boolean;
}

const SearchBar = ({ className, selectable = false, ...options }: Props) => {
  const history = useHistory();

  const [query, setQuery] = useState<string>('');
  const [techs, setTechs] = useState<Tech[]>([]);
  const [searchType, setSearchType] = useState<SearchType>(SearchType.CONTENT);

  const search = (event: React.FormEvent) => {
    event.preventDefault();

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
          <Styled.Input onChange={(event) => setQuery(event.target.value)} {...options} />
        )}
        {searchType === SearchType.TECH && (
          <TechInput
            onUpdateTechs={(techs: Tech[]) => setTechs(techs)}
            placeholder="기술스택 선택 후 우측 검색 아이콘을 클릭하세요"
          />
        )}
        <Styled.Button>
          <SearchIcon width="32px" />
        </Styled.Button>
      </Styled.Root>
    </TechTagProvider>
  );
};

export default SearchBar;
