import React from 'react';
import { Link } from 'react-router-dom';

import Skeleton from 'components/Skeleton/Skeleton';
import RegularFeedCard from 'components/RegularFeedCard/RegularFeedCard';
import ROUTE from 'constants/routes';
import useSnackbar from 'contexts/snackbar/useSnackbar';
import useSearch from 'hooks/queries/useSearch';
import { FilterType } from 'types';
import Styled, { MoreFeedsArrow } from './SearchResultContent.styles';

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
                  <RegularFeedCard feed={feed} />
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
