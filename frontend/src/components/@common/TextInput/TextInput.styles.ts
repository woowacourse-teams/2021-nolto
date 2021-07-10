import styled from 'styled-components';

import { PALETTE } from 'constants/palette';

const Root = styled.input`
  font-size: 24px;
  width: 100%;
  height: 52px;
  padding: 4px 12px;
  border: 1px solid ${PALETTE.PRIMARY_400};
  border-radius: 8px;
  outline: none;

  &:focus {
    border: 2px solid ${PALETTE.PRIMARY_400};
  }
`;

export default { Root };
