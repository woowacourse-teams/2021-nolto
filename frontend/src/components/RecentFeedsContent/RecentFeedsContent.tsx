import React, { useState } from 'react';
import { Link } from 'react-router-dom';

import LevelButton from 'components/LevelButton/LevelButton';
import StretchCard from 'components/StretchCard/StretchCard';
import useRecentFeeds from 'hooks/queries/useRecentFeeds';
import useSnackBar from 'context/snackBar/useSnackBar';
import ROUTE from 'constants/routes';
import Styled from './RecentFeedsContent.styles';
import { FilterType } from 'types';

interface Props {
  limit?: number;
}

const RecentFeedsContent = ({ limit }: Props) => {
  const [filter, setFilter] = useState<FilterType>();

  const snackbar = useSnackBar();
  const { data: recentFeeds } = useRecentFeeds({
    filter,
    errorHandler: (error) => snackbar.addSnackBar('error', error.message),
  });

  const toggleLevel = (filterType: FilterType) => {
    if (filter === filterType) setFilter(null);
    else setFilter(filterType);
  };

  return (
    <Styled.Root>
      <Styled.LevelButtonsContainer>
        <LevelButton.Progress
          onClick={() => toggleLevel(FilterType.PROGRESS)}
          selected={filter === FilterType.PROGRESS}
        />
        <LevelButton.Complete
          onClick={() => toggleLevel(FilterType.COMPLETE)}
          selected={filter === FilterType.COMPLETE}
        />
        <LevelButton.SOS
          onClick={() => toggleLevel(FilterType.SOS)}
          selected={filter === FilterType.SOS}
        />
      </Styled.LevelButtonsContainer>
      <Styled.CardsContainer>
        {recentFeeds &&
          recentFeeds.slice(0, limit).map((feed) => (
            <li key={feed.id}>
              <Link to={`${ROUTE.FEEDS}/${feed.id}`}>
                <Styled.VerticalAvatar user={feed.author} />
                <StretchCard feed={feed} />
              </Link>
            </li>
          ))}
      </Styled.CardsContainer>
    </Styled.Root>
  );
};

export default RecentFeedsContent;
