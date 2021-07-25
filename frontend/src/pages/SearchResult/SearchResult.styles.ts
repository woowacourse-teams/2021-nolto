import styled from 'styled-components';

import HighLightedText from 'components/@common/HighlightedText/HighlightedText';
import TechInputComponent from 'context/techTag/input/TechInput';
import { PALETTE } from 'constants/palette';

const Root = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 4rem 0;
`;

const SectionTitle = styled(HighLightedText)`
  margin-bottom: 1rem;
`;

const SearchBarContainer = styled.form`
  position: relative;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0 12px;
  width: 18rem;
  height: 2.5rem;
  margin-bottom: 4rem;

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

const RecentToysContainer = styled.div`
  display: flex;
  flex-direction: column;
  width: fit-content;
`;

const LevelButtonsContainer = styled.div`
  display: flex;
  justify-content: center;
  gap: 2rem;
  margin-bottom: 3.5rem;
`;

const TechTagContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 24rem;
  margin-bottom: 4rem;
`;

const TechInputWrapper = styled.div`
  width: 100%;
  background-color: ${PALETTE.WHITE_400};
  border-radius: 25px;
  filter: drop-shadow(0 0 4px rgba(0, 0, 0, 0.25));
  z-index: 10;

  & input {
    border: none;

    &:focus {
      border: none;
    }
  }
`;

export const TechInput = styled(TechInputComponent)``;

export default {
  Root,
  SectionTitle,
  SearchBarContainer,
  Input,
  Button,
  RecentToysContainer,
  LevelButtonsContainer,
  TechTagContainer,
  TechInputWrapper,
};
