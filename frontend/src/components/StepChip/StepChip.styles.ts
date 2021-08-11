import styled, { css } from 'styled-components';

import Chip from 'components/@common/Chip/Chip';
import { hoverLayer } from 'commonStyles';
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

const sos = css`
  background: ${PALETTE.PRIMARY_400};

  &::before {
    content: '🚨 ';
  }
`;

const chipStyleMap = {
  [FeedStatus.PROGRESS]: progress,
  [FeedStatus.COMPLETE]: complete,
  [FeedStatus.SOS]: sos,
};

const Button = styled.button`
  background: transparent;
  border: none;
`;

const DefaultRoot = styled(Chip.Solid)<{ step: FeedStatus }>`
  color: ${PALETTE.BLACK_400};
  ${({ step }) => chipStyleMap[step]};
`;

const ClickableRoot = styled(Chip.Solid)<{ step: FeedStatus; selected: boolean }>`
  color: ${PALETTE.BLACK_400};
  ${({ step }) => chipStyleMap[step]};
  box-shadow: ${({ selected }) => selected && 'inset'} 1px 1px 2px 1px rgba(0, 0, 0, 0.1);

  ${hoverLayer({})};
`;

export default { Button, DefaultRoot, ClickableRoot };
