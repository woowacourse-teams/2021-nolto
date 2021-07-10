import styled from 'styled-components';

import { PALETTE } from 'constants/palette';

const Root = styled.textarea`
  width: 100%;
  height: 100%;
  font-size: 24px;
  padding: 8px 16px;
  border: 1px solid ${PALETTE.PRIMARY_400};
  border-radius: 8px;
  resize: none;
`;

export default { Root };
