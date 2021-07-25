import React, { useState } from 'react';
import { Link } from 'react-router-dom';

import LevelButton from 'components/LevelButton/LevelButton';
import StretchCard from 'components/StretchCard/StretchCard';
import useRecentFeeds from 'hooks/queries/useRecentFeeds';
import ROUTE from 'constants/routes';
import Styled from './RecentFeedsContent.styles';
import { FilterType } from 'types';

interface Props {
  limit?: number;
}

const RecentFeedsContent = ({ limit }: Props) => {
  const [filterType, setFilterType] = useState<FilterType>();
  const { data: recentFeeds } = useRecentFeeds(filterType);

  const toggleLevel = (type: FilterType) => {
    if (filterType === type) setFilterType(null);
    else setFilterType(type);
  };

  return (
    <Styled.Root>
      <Styled.LevelButtonsContainer>
        <LevelButton.Progress
          onClick={() => toggleLevel(FilterType.PROGRESS)}
          selected={filterType === FilterType.PROGRESS}
        />
        <LevelButton.Complete
          onClick={() => toggleLevel(FilterType.COMPLETE)}
          selected={filterType === FilterType.COMPLETE}
        />
        <LevelButton.SOS
          onClick={() => toggleLevel(FilterType.SOS)}
          selected={filterType === FilterType.SOS}
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
