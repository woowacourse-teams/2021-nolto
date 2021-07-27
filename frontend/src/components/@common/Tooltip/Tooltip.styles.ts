import styled from 'styled-components';

import { PALETTE } from 'constants/palette';

const Root = styled.div`
  position: absolute;
  background-color: ${PALETTE.WHITE_400};
  border: 1px solid ${PALETTE.PRIMARY_400};
  border-radius: 4px;
  padding: 0.5rem 1rem;
  font-size: 0.75rem;
  z-index: 10;
`;

export default { Root };
