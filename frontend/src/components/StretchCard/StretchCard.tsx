import React from 'react';

import { Feed } from 'types';
import Chip from 'components/@common/Chip/Chip';
import SOSFlag from 'assets/sosFlag.svg';
import Styled from './StretchCard.styles';

interface Props {
  feed: Feed;
}

const StretchCard = ({ feed }: Props) => {
  return (
    <Styled.Root>
      <Styled.Thumbnail src={feed.thumbnailUrl} />
      <Styled.ChipWrapper>
        <Chip.Solid>전시중</Chip.Solid>
      </Styled.ChipWrapper>
      <Styled.ContentArea>
        <Styled.TitleWrapper>
          <Styled.Title>{feed.title}</Styled.Title>
          <SOSFlag width="56" />
        </Styled.TitleWrapper>
        <Styled.Content>{feed.content}</Styled.Content>
      </Styled.ContentArea>
    </Styled.Root>
  );
};

export default StretchCard;
