import React from 'react';

import SOSFlag from 'assets/sosFlag.svg';
import TechChip from 'components/TechChip/TechChip';
import { Feed } from 'types';
import Styled from './StretchCard.styles';

interface Props {
  feed: Feed;
}

const StretchCard = ({ feed }: Props) => {
  return (
    <Styled.Root>
      <Styled.Thumbnail src={feed.thumbnailUrl} />
      <Styled.ChipWrapper>
        <TechChip step={feed.step} />
      </Styled.ChipWrapper>
      <Styled.ContentArea>
        <Styled.TitleWrapper>
          <Styled.Title>{feed.title}</Styled.Title>
          {feed.sos && <SOSFlag width="40px" />}
        </Styled.TitleWrapper>
        <Styled.Content>{feed.content}</Styled.Content>
      </Styled.ContentArea>
    </Styled.Root>
  );
};

export default StretchCard;
