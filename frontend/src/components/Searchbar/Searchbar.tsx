import React from 'react';

import SearchIcon from 'assets/search.svg';
import Styled from './Searchbar.styles';

interface Props {
  className?: string;
}

const Searchbar = ({ className }: Props) => {
  return (
    <Styled.Root className={className}>
      <Styled.Input />
      <Styled.Button>
        <SearchIcon width="32" />
      </Styled.Button>
    </Styled.Root>
  );
};

export default Searchbar;
