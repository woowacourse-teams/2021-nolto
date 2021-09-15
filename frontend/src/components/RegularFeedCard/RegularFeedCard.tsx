import React from 'react';
import { Link } from 'react-router-dom';

import Avatar from 'components/@common/Avatar/Avatar';
import SOSFlag from 'components/@common/SOSFlag/SOSFlag';
import FeedThumbnail from 'components/FeedThumbnail/FeedThumbnail';
import { Feed } from 'types';
import Styled from './RegularFeedCard.styles';

interface Props {
  feed: Feed;
}

const RegularFeedCard = ({ feed }: Props) => {
  return (
    <Styled.Root>
      <Avatar user={feed.author} />
      {feed.sos && <SOSFlag className="sos" />}
      <Link className="link" to={`feeds/${feed.id}`}>
        <Styled.RegularCardImgWrapper>
          <FeedThumbnail thumbnailUrl={feed.thumbnailUrl} />
        </Styled.RegularCardImgWrapper>
        <Styled.Content>
          <h3>{feed.title}</h3>
          <p>{feed.content}</p>
        </Styled.Content>
      </Link>
    </Styled.Root>
  );
};

export default RegularFeedCard;
