import styled from 'styled-components';

import { PALETTE } from 'constants/palette';

const Root = styled.input`
  font-size: 1rem;
  width: 100%;
  padding: 8px 12px;
  border: 1px solid ${PALETTE.PRIMARY_400};
  border-radius: 4px;

  &:focus {
    border: 2px solid ${PALETTE.PRIMARY_400};
  }
`;

export default { Root };
