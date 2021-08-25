import Avatar from 'components/@common/Avatar/Avatar';
import React from 'react';
import { Feed } from 'types';

interface Props {
  feed: Feed;
}

const RegularCard = ({ feed }: Props) => {
  return (
    <div>
      <Avatar user={feed.author} />
    </div>
  );
};

export default RegularCard;
