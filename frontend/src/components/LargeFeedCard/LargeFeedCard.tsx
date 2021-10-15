import React from 'react';
import { Link } from 'react-router-dom';

import Avatar from 'components/@common/Avatar/Avatar';
import Thumbnail from 'components/Thumbnail/Thumbnail';
import { removeMarkdown } from 'utils/common';
import { Feed } from 'types';
import Styled from './LargeFeedCard.styles';

interface Props {
  feed: Feed;
  className?: string;
}

const LargeFeedCard = ({ feed, className }: Props) => {
  return (
    <Styled.Root className={className}>
      <Avatar user={feed.author} />
      <Link to={`feeds/${feed.id}`}>
        <Styled.FeedContainer>
          <Thumbnail thumbnailUrl={feed.thumbnailUrl} alt={feed.title} />
          <Styled.ContentWrapper className="card-content">
            <Styled.Title>{feed.title}</Styled.Title>
            <Styled.Content>{removeMarkdown(feed.content)}</Styled.Content>
          </Styled.ContentWrapper>
        </Styled.FeedContainer>
      </Link>
    </Styled.Root>
  );
};

export default LargeFeedCard;
