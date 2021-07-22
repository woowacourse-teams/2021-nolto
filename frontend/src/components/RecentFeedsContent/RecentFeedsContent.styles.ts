import styled from 'styled-components';

import Avatar from 'components/@common/Avatar/Avatar';

const Root = styled.div`
  display: flex;
  flex-direction: column;
  height: 100%;
  width: fit-content;
`;

const CardsContainer = styled.ul`
  display: flex;
  flex-direction: column;
  gap: 2rem;
  align-items: center;
  height: 100%;
  padding: 1rem;
  overflow: scroll;

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
  CardsContainer,
  VerticalAvatar,
  LevelButtonsContainer,
};
