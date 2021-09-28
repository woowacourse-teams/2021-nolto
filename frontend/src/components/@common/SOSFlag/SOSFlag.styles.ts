import styled from 'styled-components';

import { PALETTE } from 'constants/palette';

const Root = styled.div`
  z-index: 10;

  & svg {
    position: absolute;
  }
`;

const FrontSide = styled.div`
  width: 5rem;
  height: 1.75rem;
  background-color: ${PALETTE.PRIMARY_400};
  border-radius: 0 4px 4px 0;
  color: ${PALETTE.WHITE_400};
  font-size: 0.85rem;
  font-style: italic;
  text-align: center;
  line-height: 1.75rem;
  box-shadow: 1px 1px 2px ${PALETTE.PRIMARY_SHADOW};
`;

export default { Root, FrontSide };
