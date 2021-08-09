import styled, { css, keyframes } from 'styled-components';

import { PALETTE } from 'constants/palette';
import { SnackBarType } from 'types';
import Z_INDEX from 'constants/zIndex';

const show = keyframes`
  from {
    opacity: 0;
  }

  to {
    opacity: 0.8;
  }
`;

const hide = css`
  transform: translateX(-120%);
`;

const SnackBarWrapper = styled.div`
  position: fixed;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  bottom: 1rem;
  left: 1rem;
  z-index: ${Z_INDEX.SNACKBAR};
`;

const SnackBar = styled.div<{ type: SnackBarType }>`
  animation: ${show} 0.5s ease;

  display: flex;
  align-items: center;
  gap: 0.35rem;
  padding-left: 1rem;
  width: 23.75rem;
  height: 3rem;
  background-color: ${PALETTE.BLACK_400};
  opacity: 0.8;
  border-radius: 2px;
  transition: all 0.3s ease;

  ${({ type }) => type === null && hide};

  & span {
    color: ${PALETTE.WHITE_400};
  }
`;

export default { SnackBarWrapper, SnackBar };
