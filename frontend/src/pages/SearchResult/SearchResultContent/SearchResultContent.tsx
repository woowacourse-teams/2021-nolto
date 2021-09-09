import React from 'react';

import RegularFeedCard from 'components/RegularFeedCard/RegularFeedCard';
import useSnackbar from 'contexts/snackbar/useSnackbar';
import useSearch from 'hooks/queries/useSearch';
import { FilterType } from 'types';
import Styled from './SearchResultContent.styles';

interface Props {
  query: string;
  techs: string;
  filter: FilterType;
}

const SearchResultContent = (searchParams: Props) => {
  const snackbar = useSnackbar();

  const { data: feeds } = useSearch({
    searchParams,
    errorHandler: (error) => snackbar.addSnackbar('error', error.message),
    suspense: false,
  });

  return (
    <Styled.Root>
      <Styled.RecentFeedsContainer>
        {feeds?.map((feed) => (
          <RegularFeedCard key={feed.id} feed={feed} />
        ))}
      </Styled.RecentFeedsContainer>
    </Styled.Root>
  );
};

export default SearchResultContent;
