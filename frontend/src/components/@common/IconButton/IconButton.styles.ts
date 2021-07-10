import styled from 'styled-components';

import { PALETTE } from 'constants/palette';

const Root = styled.button`
  width: 52px;
  height: 52px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: ${PALETTE.WHITE_400};
  border: none;
  border-radius: 25px;
  filter: drop-shadow(0 0 4px rgba(0, 0, 0, 0.25));
`;

export default { Root };
