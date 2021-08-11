import styled from 'styled-components';

import HighLightedText from 'components/@common/HighlightedText/HighlightedText';
import TechInputComponent from 'contexts/techTag/input/TechInput';

const Root = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 4rem 0;
`;

const SectionTitle = styled(HighLightedText)`
  margin-bottom: 1rem;
`;

const StepChipsContainer = styled.div`
  display: flex;
  justify-content: center;
  gap: 0.85rem;
  margin-bottom: 3.5rem;
`;

const RecentToysContainer = styled.div`
  height: 60vh;
`;

export const TechInput = styled(TechInputComponent)``;

export default {
  Root,
  SectionTitle,
  StepChipsContainer,
  RecentToysContainer,
};
