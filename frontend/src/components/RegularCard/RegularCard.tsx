import React from 'react';

import { Feed } from 'types';
import Styled from './RegularCard.styles';

interface Props {
  feed: Feed;
}

const RegularCard = ({ feed }: Props) => {
  return (
    <Styled.Root imageUrl={feed.thumbnailUrl}>
      <Styled.ContentArea className="card-content">
        <Styled.Title>{feed.title}</Styled.Title>
        <Styled.Content>{feed.content}</Styled.Content>
      </Styled.ContentArea>
    </Styled.Root>
  );
};

export default RegularCard;
