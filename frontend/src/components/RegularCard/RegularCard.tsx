import Avatar from 'components/@common/Avatar/Avatar';
import React from 'react';
import { Feed } from 'types';
import Styled, { RegularCardImgWrapper } from './RegularCard.styles';

interface Props {
  feed: Feed;
}

const RegularCard = ({ feed }: Props) => {
  return (
    <Styled.Root>
      <Avatar user={feed.author} />
      <RegularCardImgWrapper>
        <img className="project-image" src={feed.thumbnailUrl} />
      </RegularCardImgWrapper>
      <Styled.Content>
        <h3>{feed.title}</h3>
        <p>{feed.content}</p>
      </Styled.Content>
    </Styled.Root>
  );
};

export default RegularCard;
