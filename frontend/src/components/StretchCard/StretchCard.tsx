import React from 'react';

import { Feed } from 'types';
import Chip from 'components/@common/Chip/Chip';
import { STEP_CONVERTER } from 'constants/common';
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
        <Chip.Solid>{STEP_CONVERTER[feed.step]}</Chip.Solid>
      </Styled.ChipWrapper>
      <div>
        <Styled.TitleWrapper>
          <Styled.Title>{feed.title}</Styled.Title>
          {feed.sos && <SOSFlag width="40px" />}
        </Styled.TitleWrapper>
        <Styled.Content>{feed.content}</Styled.Content>
      </div>
    </Styled.Root>
  );
};

export default StretchCard;
