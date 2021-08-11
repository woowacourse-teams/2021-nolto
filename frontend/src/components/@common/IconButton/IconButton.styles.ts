import styled from 'styled-components';

import { PALETTE } from 'constants/palette';
import { hoverLayer } from 'commonStyles';

const Root = styled.button<{ isShadow: boolean }>`
  display: flex;
  width: fit-content;
  flex-shrink: 0;
  justify-content: center;
  align-items: center;
  background-color: ${PALETTE.WHITE_400};
  border: none;
  border-radius: 50%;
  filter: ${({ isShadow }) => isShadow && 'drop-shadow(0 0 4px rgba(0, 0, 0, 0.25))'};
  overflow: hidden;

  & > * {
    padding: 15%;
    flex-shrink: 0;
  }

  ${hoverLayer({})};
`;

export default { Root };
