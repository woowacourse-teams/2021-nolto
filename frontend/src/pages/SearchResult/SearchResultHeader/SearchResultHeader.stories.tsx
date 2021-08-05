import React from 'react';

import SearchResultHeader from './SearchResultHeader';

export default {
  title: 'components/SearchResultHeader',
  component: SearchResultHeader,
  argTypes: {},
};

export const Default = () => (
  <SearchResultHeader
    searchParams=""
    query=""
    setQuery={() => console.log('set query')}
    techs=""
    setTechs={() => console.log('set techs')}
  />
);
