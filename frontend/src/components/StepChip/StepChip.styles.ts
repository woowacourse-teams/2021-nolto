import styled, { css } from 'styled-components';

import Chip from 'components/@common/Chip/Chip';
import { hoverLayer } from 'commonStyles';
import { FeedStep } from 'types';
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
  [FeedStep.PROGRESS]: progress,
  [FeedStep.COMPLETE]: complete,
};

const Root = styled(Chip.Solid)<{ step: FeedStep; selected: boolean }>`
  color: ${PALETTE.BLACK_400};
  ${({ step }) => chipStyleMap[step]};
  box-shadow: ${({ selected }) => selected && 'inset'} 1px 1px 2px 1px rgba(0, 0, 0, 0.4);

  ${hoverLayer({})};
`;

export default { Root };
