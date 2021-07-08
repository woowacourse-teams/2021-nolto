import React from 'react';

import { Feed } from 'types';
import Styled from './StretchCard.styles';

interface Props {
  feed: Feed;
}

const StretchCard = ({ feed }: Props) => {
  return (
    <Styled.Root>
      <Styled.Thumbnail src={feed.thumbnailUrl} />
      <Styled.ContentArea>
        <Styled.Title>{feed.title}</Styled.Title>
        <Styled.Content>{feed.content}</Styled.Content>
      </Styled.ContentArea>
    </Styled.Root>
  );
};

export default StretchCard;
