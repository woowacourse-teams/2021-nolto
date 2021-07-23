import styled from 'styled-components';

import HighLightedText from 'components/@common/HighlightedText/HighlightedText';
import Avatar from 'components/@common/Avatar/Avatar';

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

const RecentToyCardsContainer = styled.ul`
  display: flex;
  flex-direction: column;
  gap: 2rem;
  align-items: center;
  height: 40rem;
  padding: 0 1rem;
  overflow: auto;

  &::-webkit-scrollbar {
    display: none;
  }

  /* Hide scrollbar for IE, Edge and Firefox */
  -ms-overflow-style: none;
  scrollbar-width: none;
`;

const VerticalAvatar = styled(Avatar)`
  margin-bottom: 12px;
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
  RecentToyCardsContainer,
  VerticalAvatar,
  LevelButtonsContainer,
};
