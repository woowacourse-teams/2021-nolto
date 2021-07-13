import { PALETTE } from 'constants/palette';
import styled from 'styled-components';

const Root = styled.input`
  font-size: 24px;
  border: none;
  border-bottom: 1px solid ${PALETTE.PRIMARY_400};
  padding: 4px 8px;

  &:focus {
    border-bottom: 2px solid ${PALETTE.PRIMARY_400};
  }
`;

export default { Root };
