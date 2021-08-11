import styled from 'styled-components';

import { PALETTE } from 'constants/palette';
import Z_INDEX from 'constants/zIndex';

const Root = styled.div`
  position: relative;
`;

const Dropdown = styled.ul`
  position: absolute;
  background-color: ${PALETTE.WHITE_400};
  width: 100%;
  margin-top: 2px;
  max-height: 20rem;
  overflow-y: scroll;
  z-index: ${Z_INDEX.DROPDOWN};
`;

const TechOption = styled.li<{ focused?: boolean }>`
  font-size: 1rem;
  width: 100%;
  padding: 8px 12px;
  border: 1px solid ${PALETTE.PRIMARY_400};
  background-color: ${({ focused }) => focused && PALETTE.PRIMARY_400};
  color: ${({ focused }) => focused && PALETTE.WHITE_400};

  &:hover {
    background-color: ${PALETTE.PRIMARY_400};
    color: ${PALETTE.WHITE_400};
  }

  &:first-child {
    border-top-left-radius: 8px;
    border-top-right-radius: 8px;
  }

  &:last-child {
    border-bottom-left-radius: 8px;
    border-bottom-right-radius: 8px;
  }
`;

export default { Root, Dropdown, TechOption };
