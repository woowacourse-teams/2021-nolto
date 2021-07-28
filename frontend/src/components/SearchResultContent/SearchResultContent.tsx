import React from 'react';
import { Link } from 'react-router-dom';

import StretchCard from 'components/StretchCard/StretchCard';
import ROUTE from 'constants/routes';
import useSnackBar from 'context/snackBar/useSnackBar';
import useSearch from 'hooks/queries/useSearch';
import Styled from './SearchResultContent.styles';
import { FilterType } from 'types';

interface Props {
  query: string;
  techs: string;
  filter: FilterType;
}

const SearchResultContent = (searchParams: Props) => {
  const snackbar = useSnackBar();

  const { data: feeds } = useSearch({
    searchParams,
    errorHandler: (error) => snackbar.addSnackBar('error', error.message),
    suspense: false,
  });

  return (
    <Styled.Root>
      {feeds?.map((feed) => (
        <li key={feed.id}>
          <Link to={`${ROUTE.FEEDS}/${feed.id}`}>
            <Styled.VerticalAvatar user={feed.author} />
            <StretchCard feed={feed} />
          </Link>
        </li>
      ))}
    </Styled.Root>
  );
};

export default SearchResultContent;
