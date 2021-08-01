import styled, { keyframes } from 'styled-components';

import DownPolygon from 'assets/downPolygon.svg';
import { PALETTE } from 'constants/palette';

const opacityAnimation = keyframes`
  from {
    opacity: 0;
  }

  to {
    opacity : 1;
  }
`;

const OPACITY_ANIMATION_TIME = '0.5s';

const Root = styled.div<{ isOpen: boolean }>`
  height: ${({ isOpen }) => (isOpen ? '7rem' : '2.5rem')};
  min-width: 108px;
  background-color: ${PALETTE.WHITE_400};
  align-self: flex-start;
  border-radius: 25px;
  filter: drop-shadow(0 0 4px rgba(0, 0, 0, 0.25));
  transition: height 0.3s ease;
`;

const DefaultSelector = styled.span`
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 0.4rem;
  line-height: 1rem;
  cursor: pointer;
`;

export const SearchMorePolygon = styled(DownPolygon)<{ isOpen: boolean }>`
  transform: ${({ isOpen }) => isOpen && 'rotate(180deg)'};
  margin-bottom: ${({ isOpen }) => (isOpen ? '0.5rem' : '0.25rem')};
`;

const SearchOptionText = styled.div`
  color: ${PALETTE.PRIMARY_400};
  cursor: pointer;
  text-align: center;
  line-height: 1.15rem;
  margin: 0.65rem 0;

  animation: ${opacityAnimation} ${OPACITY_ANIMATION_TIME} ease;

  &.option {
    &:hover {
      text-decoration: underline;
    }
  }
`;

export default {
  Root,
  DefaultSelector,
  SearchOptionText,
};
