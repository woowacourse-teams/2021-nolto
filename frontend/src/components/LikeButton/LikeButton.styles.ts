import { PALETTE } from 'constants/palette';
import styled from 'styled-components';

const Root = styled.div`
  display: flex;
  gap: 0.25rem;
  align-items: center;
`;

export const Button = styled.button`
  display: flex;
  align-items: center;
  justify-content: center;
  width: 1.5rem;
  height: 1.5rem;
  background: none;
  border: none;

  &:hover {
    & > svg {
      fill: ${PALETTE.RED_400};
    }
  }
`;

export default { Root };
