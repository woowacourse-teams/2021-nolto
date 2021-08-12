import React from 'react';
import { Link } from 'react-router-dom';

import StretchCard from 'components/StretchCard/StretchCard';
import Skeleton from 'components/Skeleton/Skeleton';
import ROUTE from 'constants/routes';
import useSnackbar from 'contexts/snackbar/useSnackbar';
import useSearch from 'hooks/queries/useSearch';
import Styled, { MoreFeedsArrow } from './SearchResultContent.styles';
import { FilterType } from 'types';

interface Props {
  query: string;
  techs: string;
  filter: FilterType;
}

const SearchResultContent = (searchParams: Props) => {
  const snackbar = useSnackbar();

  const { data: feeds, isLoading } = useSearch({
    searchParams,
    errorHandler: (error) => snackbar.addSnackbar('error', error.message),
    suspense: false,
  });

  const isOverflown = feeds?.length >= 3;

  const DEFAULT_FEED_LENGTH = 4;

  return (
    <Styled.Root>
      <Styled.ScrollableContainer>
        {isLoading
          ? Array.from({ length: DEFAULT_FEED_LENGTH }, () => <Skeleton />)
          : feeds?.map((feed) => (
              <li key={feed.id}>
                <Link to={`${ROUTE.FEEDS}/${feed.id}`}>
                  <Styled.VerticalAvatar user={feed.author} />
                  <StretchCard feed={feed} />
                </Link>
              </li>
            ))}
      </Styled.ScrollableContainer>

      {isOverflown && (
        <Styled.MoreButton>
          <MoreFeedsArrow width="14px" />
        </Styled.MoreButton>
      )}
    </Styled.Root>
  );
};

export default SearchResultContent;
