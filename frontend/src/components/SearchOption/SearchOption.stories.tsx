import React from 'react';
import { SearchType } from 'types';

import SearchOption from './SearchOption';

export default {
  title: 'components/SearchOption',
  component: SearchOption,
  argTypes: {},
};

export const Default = () => (
  <SearchOption
    searchType={SearchType.CONTENT}
    setSearchType={() => console.log('change search type')}
  />
);
