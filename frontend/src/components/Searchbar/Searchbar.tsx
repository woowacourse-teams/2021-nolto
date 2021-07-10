import React from 'react';

import SearchIcon from 'assets/search.svg';
import Styled from './Searchbar.styles';

const Searchbar = () => {
  return (
    <Styled.Root>
      <Styled.Input />
      <Styled.Button>
        <SearchIcon />
      </Styled.Button>
    </Styled.Root>
  );
};

export default Searchbar;
