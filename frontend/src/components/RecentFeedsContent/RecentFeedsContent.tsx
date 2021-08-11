import React, { useState } from 'react';
import { Link } from 'react-router-dom';

import LevelButton from 'components/LevelButton/LevelButton';
import StretchCard from 'components/StretchCard/StretchCard';
import Skeleton from 'components/Skeleton/Skeleton';
import useRecentFeedsLoad from 'hooks/queries/feed/useRecentFeedsLoad';
import useSnackbar from 'contexts/snackbar/useSnackbar';
import ROUTE from 'constants/routes';
import Styled, { MoreFeedsArrow } from './RecentFeedsContent.styles';
import { FilterType } from 'types';

interface Props {
  feedsCountToShow?: number;
}

const RecentFeedsContent = ({ feedsCountToShow }: Props) => {
  const [filter, setFilter] = useState<FilterType>();

  const snackbar = useSnackbar();
  const { data: recentFeeds, isLoading } = useRecentFeedsLoad({
    filter,
    errorHandler: (error) => snackbar.addSnackbar('error', error.message),
    suspense: false,
  });

  const toggleLevel = (filterType: FilterType) => {
    if (filter === filterType) setFilter(null);
    else setFilter(filterType);
  };

  const DEFAULT_FEED_LENGTH = 4;

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
        <Styled.ScrollableContainer>
          {isLoading
            ? Array.from({ length: feedsCountToShow || DEFAULT_FEED_LENGTH }, () => <Skeleton />)
            : recentFeeds.slice(0, feedsCountToShow).map((feed) => (
                <li key={feed.id}>
                  <Link to={`${ROUTE.FEEDS}/${feed.id}`}>
                    <Styled.VerticalAvatar user={feed.author} />
                    <StretchCard feed={feed} />
                  </Link>
                </li>
              ))}
        </Styled.ScrollableContainer>
        {!feedsCountToShow && (
          <Styled.MoreButton>
            <MoreFeedsArrow width="14px" />
          </Styled.MoreButton>
        )}
      </Styled.CardsContainer>
    </Styled.Root>
  );
};

export default RecentFeedsContent;
