import styled, { css } from 'styled-components';

import Chip from 'components/@common/Chip/Chip';
import { FeedStatus } from 'types';
import { PALETTE } from 'constants/palette';

const progress = css`
  background: ${PALETTE.PRIMARY_200};

  &::before {
    content: '🧩 ';
  }
`;

const complete = css`
  background: ${PALETTE.PRIMARY_300};

  &::before {
    content: '🦄 ';
  }
`;

const chipStyleMap = {
  [FeedStatus.PROGRESS]: progress,
  [FeedStatus.COMPLETE]: complete,
};

const Root = styled(Chip.Solid)<{ step: FeedStatus }>`
  color: ${PALETTE.BLACK_400};
  ${({ step }) => chipStyleMap[step]};
`;

export default { Root };
