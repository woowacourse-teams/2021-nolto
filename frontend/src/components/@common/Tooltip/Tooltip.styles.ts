import styled from 'styled-components';

import { PALETTE } from 'constants/palette';
import { FONT_SIZE, Z_INDEX } from 'constants/styles';

const Root = styled.div`
  position: absolute;
  background-color: ${PALETTE.WHITE_400};
  border: 1px solid ${PALETTE.PRIMARY_400};
  border-radius: 4px;
  padding: 0.5rem 1rem;
  font-size: ${FONT_SIZE.SMALL};
  z-index: ${Z_INDEX.TOOLTIP};
`;

export default { Root };
