import React, { useState } from 'react';

import SearchIcon from 'assets/search.svg';
import Styled, { SearchMorePolygon, TechChips, TechInput } from './SearchBar.styles';
// import TechInput from 'context/techTag/input/TechInput';
import { Tech } from 'types';
// import TechChips from 'context/techTag/chip/TechChips';
import TechTagProvider from 'context/techTag/TechTagProvider';
import { useHistory } from 'react-router-dom';
import ROUTE from 'constants/routes';

interface Props {
  className?: string;
  selectable?: boolean;
}

type SearchOption = '제목/내용' | '기술스택' | '검색';

const SearchBar = ({ className, selectable = false }: Props) => {
  const history = useHistory();

  const [isOptionOpened, setIsOptionOpened] = useState(false);
  const [query, setQuery] = useState<string>('');
  const [techs, setTechs] = useState<Tech[]>([]);
  const [searchOption, setSearchOption] = useState<SearchOption>('검색');

  const changeSearchOption = (option: SearchOption) => {
    setSearchOption(option);
    setIsOptionOpened(false);
  };

  const searchOptions: React.ReactNode = (
    <Styled.SearchOptionContainer isOpen={isOptionOpened}>
      <Styled.SearchTypeSelector onClick={() => setIsOptionOpened(!isOptionOpened)}>
        <Styled.SearchOptionText>{searchOption}</Styled.SearchOptionText>
        <SearchMorePolygon width="14px" isOpened={isOptionOpened} />
      </Styled.SearchTypeSelector>
      {isOptionOpened && (
        <>
          <Styled.SearchOptionText
            className="option"
            onClick={() => changeSearchOption('제목/내용')}
          >
            제목/내용
          </Styled.SearchOptionText>
          <Styled.SearchOptionText
            className="option"
            onClick={() => changeSearchOption('기술스택')}
          >
            기술스택
          </Styled.SearchOptionText>
        </>
      )}
    </Styled.SearchOptionContainer>
  );

  const search = () => {
    const queryParams = new URLSearchParams({
      query,
      techs: techs.map((tech) => tech.text).join(','),
    });

    history.push({
      pathname: ROUTE.SEARCH,
      search: '?' + queryParams,
    });
  };

  return (
    <TechTagProvider>
      Selected: <TechChips reverse={true} />
      <Styled.Root className={className} selectable={selectable} onSubmit={search}>
        {selectable && searchOptions}
        {/* <Styled.Input /> */}
        <TechInput onUpdateTechs={(techs: Tech[]) => setTechs(techs)} />
        <Styled.Button>
          <SearchIcon width="32px" />
        </Styled.Button>
      </Styled.Root>
    </TechTagProvider>
  );
};

export default SearchBar;
