import React from 'react';
import { Link } from 'react-router-dom';

import Avatar from 'components/@common/Avatar/Avatar';
import SOSFlag from 'components/@common/SOSFlag/SOSFlag';
import Thumbnail from 'components/Thumbnail/Thumbnail';
import { removeMarkdown } from 'utils/common';
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
          <Thumbnail thumbnailUrl={feed.thumbnailUrl} alt={feed.title} />
        </Styled.RegularCardImgWrapper>
        <Styled.Content>
          <h3>{feed.title}</h3>
          <p>{removeMarkdown(feed.content)}</p>
        </Styled.Content>
      </Link>
    </Styled.Root>
  );
};

export default RegularFeedCard;
