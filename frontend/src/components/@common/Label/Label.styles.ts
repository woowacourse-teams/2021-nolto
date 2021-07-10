import styled from 'styled-components';

import { PALETTE } from 'constants/palette';

const Root = styled.label`
  font-size: 24px;

  &::before {
    content: '*';
    color: ${PALETTE.RED_400};
    margin-right: 8px;
    line-height: 24px;
  }
`;

export default { Root };
