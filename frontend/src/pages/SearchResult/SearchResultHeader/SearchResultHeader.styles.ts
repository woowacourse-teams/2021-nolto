import styled from 'styled-components';

import TechInputComponent from 'context/techTag/input/TechInput';
import { PALETTE } from 'constants/palette';
import Z_INDEX from 'constants/zIndex';

const SearchBarContainer = styled.form`
  position: relative;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0 12px;
  width: 18rem;
  height: 2.5rem;
  margin-bottom: 2rem;

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
  width: 2em;
  height: 2em;
`;

const TechTagContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 24rem;
  margin-bottom: 2rem;
`;

const TechInputWrapper = styled.div`
  width: 100%;
  background-color: ${PALETTE.WHITE_400};
  border-radius: 25px;
  filter: drop-shadow(0 0 4px rgba(0, 0, 0, 0.25));
  z-index: ${Z_INDEX.TECH_INPUT};

  & input {
    border: none;

    &:focus {
      border: none;
    }
  }
`;

export const TechInput = styled(TechInputComponent)``;

export default {
  SearchBarContainer,
  Input,
  Button,
  TechTagContainer,
  TechInputWrapper,
};
