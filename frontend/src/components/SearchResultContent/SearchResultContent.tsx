import React from 'react';
import { Link } from 'react-router-dom';

import StretchCard from 'components/StretchCard/StretchCard';
import ROUTE from 'constants/routes';
import useSearch from 'hooks/queries/useSearch';
import Styled from './SearchResultContent.styles';

interface Props {
  query: string;
  techs: string;
}

const SearchResultContent = ({ query, techs }: Props) => {
  const { data: feeds } = useSearch({ query, techs });

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
