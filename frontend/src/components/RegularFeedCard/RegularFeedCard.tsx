import Avatar from 'components/@common/Avatar/Avatar';
import React from 'react';
import { Link } from 'react-router-dom';
import { Feed } from 'types';
import Styled, { RegularCardImgWrapper } from './RegularFeedCard.styles';

interface Props {
  feed: Feed;
}

const RegularFeedCard = ({ feed }: Props) => {
  return (
    <Styled.Root>
      <Avatar user={feed.author} />
      <Link className="link" to={`feeds/${feed.id}`}>
        <RegularCardImgWrapper>
          <img className="project-image" src={feed.thumbnailUrl} />
        </RegularCardImgWrapper>
        <Styled.Content>
          <h3>{feed.title}</h3>
          <p>{feed.content}</p>
        </Styled.Content>
      </Link>
    </Styled.Root>
  );
};

export default RegularFeedCard;
