import styled from 'styled-components';

import { PALETTE } from 'constants/palette';
import DownPolygon from 'assets/downPolygon.svg';

const Root = styled.form<{ selectable: boolean }>`
  position: relative;
  display: flex;
  align-items: center;
  gap: 16px;
  padding-right: 12px;
  padding-left: ${({ selectable }) => !selectable && '24px'};

  background-color: ${PALETTE.WHITE_400};
  border-radius: 25px;
  filter: drop-shadow(0 0 4px rgba(0, 0, 0, 0.25));
`;

const Input = styled.input`
  font-size: 1rem;
  flex-grow: 1;
  outline: none;
  border: none;
`;

const Button = styled.button`
  border: none;
  background-color: transparent;
  width: 2em;
  height: 2em;
`;

const SearchTypeSelector = styled.span`
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 0.65rem;
  line-height: 1rem;
  cursor: pointer;
`;

export const SearchMorePolygon = styled(DownPolygon)<{ isOpened: boolean }>`
  transform: ${({ isOpened }) => isOpened && 'rotate(180deg)'};
  margin-bottom: 0.25rem;
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

const SearchOptionContainer = styled.div<{ isOpen: boolean }>`
  width: 108px;
  background-color: ${PALETTE.WHITE_400};
  align-self: flex-start;
  border-radius: 25px;
  filter: drop-shadow(0 0 4px rgba(0, 0, 0, 0.25));
  transition: max-height 0.5s ease-in-out;
`;

export default {
  Root,
  Input,
  Button,
  SearchTypeSelector,
  SearchOptionText,
  SearchOptionContainer,
};
