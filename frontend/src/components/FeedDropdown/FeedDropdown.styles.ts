import styled from 'styled-components';

import { PALETTE } from 'constants/palette';

const Root = styled.div`
  & > .delete-button {
    color: ${PALETTE.RED_400};
  }
`;

export default { Root };
