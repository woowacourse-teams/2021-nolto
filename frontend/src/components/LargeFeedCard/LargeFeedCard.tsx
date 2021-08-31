import React, { SyntheticEvent } from 'react';
import { Link } from 'react-router-dom';

import Avatar from 'components/@common/Avatar/Avatar';
import { Feed } from 'types';
import Styled from './LargeFeedCard.styles';
import { FlexContainer } from 'commonStyles';
import { DEFAULT_IMG } from 'constants/common';

interface Props {
  feed: Feed;
}

const LargeFeedCard = ({ feed }: Props) => {
  return (
    <FlexContainer flexDirection="column" gap="1rem">
      <Avatar user={feed.author} />
      <Link to={`feeds/${feed.id}`}>
        <Styled.FeedContainer>
          <img
            src={feed.thumbnailUrl}
            onError={(event: SyntheticEvent<HTMLImageElement>) => {
              event.currentTarget.src = DEFAULT_IMG.FEED;
            }}
          />
          <Styled.ContentWrapper className="card-content">
            <Styled.Title>{feed.title}</Styled.Title>
            <Styled.Content>{feed.content}</Styled.Content>
          </Styled.ContentWrapper>
        </Styled.FeedContainer>
      </Link>
    </FlexContainer>
  );
};

export default LargeFeedCard;
