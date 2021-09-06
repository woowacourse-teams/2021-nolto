import styled from 'styled-components';

import HighLightedText from 'components/@common/HighlightedText/HighlightedText';
import TechInputComponent from 'contexts/techTag/input/TechInput';

const TopContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  padding-top: 4rem;
  padding-bottom: 2rem;
  z-index: 10;
`;

const SectionTitle = styled(HighLightedText)`
  margin-bottom: 1rem;
`;

const StepChipsContainer = styled.div`
  display: flex;
  justify-content: center;
  gap: 0.85rem;
`;

const Button = styled.button`
  background: transparent;
  border: none;
`;

export default {
  TopContainer,
  SectionTitle,
  StepChipsContainer,
  Button,
};
