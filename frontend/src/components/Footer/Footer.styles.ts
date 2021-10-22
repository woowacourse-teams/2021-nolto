import styled from 'styled-components';

import { PALETTE } from 'constants/palette';

const Root = styled.section`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: ${PALETTE.PRIMARY_400};
  width: 100%;
  height: 300px;
  gap: 30px;

  * {
    color: ${PALETTE.WHITE_400};
  }
`;

export default { Root };
