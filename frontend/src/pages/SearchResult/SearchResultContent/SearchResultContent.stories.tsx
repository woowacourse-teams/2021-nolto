import React from 'react';

import SearchResultContent from './SearchResultContent';
import { FilterType } from 'types';

export default {
  title: 'components/SearchResultContent',
  component: SearchResultContent,
  argTypes: {},
};

export const Default = () => (
  <SearchResultContent query="" techs="reactjs" filter={FilterType.COMPLETE} />
);
