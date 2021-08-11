import React, { useState } from 'react';

import { PALETTE } from 'constants/palette';
import { SearchType } from 'types';
import Styled, { SearchMorePolygon } from './SearchOption.styles';

interface Props {
  searchType: SearchType;
  setSearchType: React.Dispatch<React.SetStateAction<SearchType>>;
}

const SearchOption = ({ searchType, setSearchType }: Props) => {
  const [isOptionOpened, setIsOptionOpened] = useState(false);

  const changeSearchOption = (option: SearchType) => {
    setSearchType(option);
    setIsOptionOpened(false);
  };

  return (
    <Styled.Root isOpen={isOptionOpened}>
      <Styled.DefaultSelector onClick={() => setIsOptionOpened(!isOptionOpened)}>
        <Styled.SearchOptionText>{searchType}</Styled.SearchOptionText>
        <SearchMorePolygon width="12px" fill={PALETTE.PRIMARY_400} isOpen={isOptionOpened} />
      </Styled.DefaultSelector>
      {isOptionOpened && (
        <>
          <Styled.SearchOptionText
            className="option"
            onClick={() => changeSearchOption(SearchType.CONTENT)}
          >
            제목/내용
          </Styled.SearchOptionText>
          <Styled.SearchOptionText
            className="option"
            onClick={() => changeSearchOption(SearchType.TECH)}
          >
            기술스택
          </Styled.SearchOptionText>
        </>
      )}
    </Styled.Root>
  );
};

export default SearchOption;
