import styled, { css } from 'styled-components';

import { PALETTE } from 'constants/palette';

const asterisk = css`
  &::before {
    content: '*';
    color: ${PALETTE.RED_400};
    margin-right: 8px;
    line-height: 24px;
  }
`;

const Root = styled.label<{ required: boolean }>`
  font-size: 24px;

  ${({ required }) => required && asterisk}
`;

export default { Root };
