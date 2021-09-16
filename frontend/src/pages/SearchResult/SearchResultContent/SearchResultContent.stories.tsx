import React from 'react';

import SearchResultContent from './SearchResultContent';
import { FeedStep } from 'types';

export default {
  title: 'components/SearchResultContent',
  component: SearchResultContent,
  argTypes: {},
};

export const Default = () => (
  <SearchResultContent
    searchParams={{ query: '', techs: 'reactjs' }}
    step={FeedStep.COMPLETE}
    help
  />
);
