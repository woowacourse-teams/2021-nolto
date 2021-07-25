import styled from 'styled-components';

import DownPolygon from 'assets/downPolygon.svg';
import { PALETTE } from 'constants/palette';

const Root = styled.div<{ isOpen: boolean }>`
  min-width: 108px;
  background-color: ${PALETTE.WHITE_400};
  align-self: flex-start;
  border-radius: 25px;
  filter: drop-shadow(0 0 4px rgba(0, 0, 0, 0.25));
`;

const DefaultSelector = styled.span`
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 0.65rem;
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
