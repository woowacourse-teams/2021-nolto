import styled from 'styled-components';

import HighLightedText from 'components/@common/HighlightedText/HighlightedText';

const Root = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 4rem 0;
`;

const SectionTitle = styled(HighLightedText)`
  margin-bottom: 48px;
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

export default {
  Root,
  SectionTitle,
  RecentToysContainer,
  LevelButtonsContainer,
};
