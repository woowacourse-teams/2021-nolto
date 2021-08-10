import styled from 'styled-components';

import { PALETTE } from 'constants/palette';
import { MEDIA_QUERY } from 'constants/mediaQuery';
import TechChipsComponent from 'contexts/techTag/chip/TechChips';
import TechInputComponent from 'contexts/techTag/input/TechInput';

const Root = styled.form<{ selectable: boolean }>`
  position: relative;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding-right: 12px;
  padding-left: ${({ selectable }) => !selectable && '24px'};

  background-color: ${PALETTE.WHITE_400};
  border-radius: 25px;
  filter: drop-shadow(0 0 4px rgba(0, 0, 0, 0.25));
`;

const Input = styled.input`
  font-size: 1rem;
  flex-grow: 1;
  padding: 0 0.25rem;
  outline: none;
  border: none;
`;

const Button = styled.button`
  border: none;
  background-color: transparent;
  width: 2rem;
  height: 2rem;

  @media ${MEDIA_QUERY.TABLET} {
    width: 1.75rem;
    height: 1.75rem;
  }

  @media ${MEDIA_QUERY.MOBILE} {
    width: 1.5rem;
    height: 1.5rem;
  }
`;

const SelectedChips = styled.div`
  width: 100%;
  height: 36px;
  display: flex;
  align-items: center;

  & > span {
    color: ${PALETTE.WHITE_400};

    &:after {
      content: '';
      margin-right: 0.25rem;
    }
  }
`;

export const TechChips = styled(TechChipsComponent)``;

export const TechInput = styled(TechInputComponent)`
  width: 100%;

  & > input {
    border: none;

    &:focus {
      border: none;
    }
  }
`;

export default {
  Root,
  Input,
  Button,
  SelectedChips,
};
