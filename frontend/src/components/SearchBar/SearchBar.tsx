import React, { useState } from 'react';

import SearchIcon from 'assets/search.svg';
import Styled, { SearchMorePolygon } from './SearchBar.styles';

interface Props {
  className?: string;
  selectable?: boolean;
}

type SearchOption = '제목/내용' | '기술스택' | '검색';

const SearchBar = ({ className, selectable = false }: Props) => {
  const [isOptionOpened, setIsOptionOpened] = useState(false);
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

  return (
    <Styled.Root className={className} selectable={selectable}>
      {selectable && searchOptions}
      <Styled.Input />
      <Styled.Button>
        <SearchIcon width="32px" />
      </Styled.Button>
    </Styled.Root>
  );
};

export default SearchBar;
