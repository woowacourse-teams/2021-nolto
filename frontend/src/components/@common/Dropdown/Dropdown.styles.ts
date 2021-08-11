import { PALETTE } from 'constants/palette';
import styled, { css } from 'styled-components';

const Root = styled.div`
  position: relative;
  display: flex;
  justify-content: center;
  height: 16px;
`;

const trigger = css`
  background-color: ${PALETTE.GRAY_300};
  filter: ${`drop-shadow(0 0 2px ${PALETTE.BLACK_100})`};
`;

export const ToggleButton = styled.button<{ isTriggered: boolean }>`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 40px;
  height: 100%;
  background-color: transparent;
  border-radius: 25px;
  border: none;

  &:hover {
    background-color: ${PALETTE.GRAY_400};
  }

  ${({ isTriggered }) => isTriggered && trigger}
`;

const DotsWrapper = styled.div`
  display: flex;
  justify-content: space-around;
  align-items: center;
`;

const Dot = styled.div`
  width: 4.5px;
  height: 4.5px;
  border-radius: 50%;
  background-color: ${PALETTE.BLACK_400};
`;

const Menu = styled.div<{ isTriggered: boolean }>`
  position: absolute;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 54px;

  border: 1px solid ${PALETTE.GRAY_400};
  border-radius: 4px;
  overflow: hidden;
  top: 130%;
  display: ${({ isTriggered }) => !isTriggered && 'none'};

  & > button {
    width: 120%;
    border: none;
    border-bottom: 1px solid ${PALETTE.GRAY_400};
    background-color: ${PALETTE.WHITE_400};
    user-select: none;
    padding: 7px 14px;

    &:last-child {
      border-bottom: none;
    }

    &:hover {
      background-color: ${PALETTE.GRAY_300};
    }
  }

  & .delete-button {
    color: ${PALETTE.RED_400};
  }
`;

export default { Root, DotsWrapper, Dot, Menu };
