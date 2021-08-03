import styled, { keyframes } from 'styled-components';

import { PALETTE } from 'constants/palette';
import TextButton from 'components/@common/TextButton/TextButton';
import Z_INDEX from 'constants/zIndex';

const show = keyframes`
  from {
    transform: scale(0.75);
  }

  to {
    transform: scale(1.0);
  }
`;

const DialogContainer = styled.div`
  position: fixed;
  display: flex;
  justify-content: center;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.15);
  z-index: ${Z_INDEX.DIALOG};
`;

const DialogInner = styled.div`
  animation: ${show} 0.1s ease;

  width: 372px;
  height: 218px;
  border-radius: 8px;
  background-color: ${PALETTE.WHITE_400};
  box-shadow: 4px 4px 4px rgba(0, 0, 0, 0.25);
  display: flex;
  flex-direction: column;
  justify-content: center;
  text-align: center;
  position: relative;
  overflow: hidden;
`;

const TopBar = styled.div`
  position: absolute;
  display: flex;
  top: 0;
  width: 100%;
  height: 40px;
  background-color: ${PALETTE.PRIMARY_400};
  align-items: center;
  justify-content: space-between;
  padding: 0 0.75rem;
`;

const AlertTitle = styled.span`
  color: ${PALETTE.WHITE_400};
`;

const CrossMarkButton = styled.button`
  border: none;
  background: transparent;
`;

const ButtonsContainer = styled.div`
  width: 100%;
  position: absolute;
  bottom: 1rem;
  padding: 0 1rem;
  display: flex;
`;

export const Button = styled(TextButton.Regular)<{ single?: boolean }>`
  width: ${({ single }) => (single ? '50%' : '100%')};
  margin: 0 ${({ single }) => (single ? 'auto' : '0.25rem')};
  padding: 0.4rem 1rem;
`;

export default {
  DialogContainer,
  DialogInner,
  TopBar,
  AlertTitle,
  CrossMarkButton,
  ButtonsContainer,
};
