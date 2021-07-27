import styled from 'styled-components';

import HighLightedText from 'components/@common/HighlightedText/HighlightedText';
import TechInputComponent from 'context/techTag/input/TechInput';

const Root = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 4rem 0;
`;

const SectionTitle = styled(HighLightedText)`
  margin-bottom: 1rem;
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

export const TechInput = styled(TechInputComponent)``;

export default {
  Root,
  SectionTitle,
  RecentToysContainer,
  LevelButtonsContainer,
};
