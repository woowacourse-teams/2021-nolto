import React from 'react';

import SearchIcon from 'assets/search.svg';
import Styled from './SearchBar.styles';

interface Props {
  className?: string;
}

const SearchBar = ({ className }: Props) => {
  return (
    <Styled.Root className={className}>
      <Styled.Input />
      <Styled.Button>
        <SearchIcon width="32px" />
      </Styled.Button>
    </Styled.Root>
  );
};

export default SearchBar;
