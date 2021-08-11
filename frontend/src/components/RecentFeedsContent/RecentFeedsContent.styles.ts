import styled from 'styled-components';

import Avatar from 'components/@common/Avatar/Avatar';
import DownPolygon from 'assets/downPolygon.svg';
import { MEDIA_QUERY } from 'constants/mediaQuery';
import { PALETTE } from 'constants/palette';
import { hoverLayer } from 'commonStyles';

const Root = styled.div`
  display: flex;
  flex-direction: column;
  height: 100%;
`;

const CardsContainer = styled.div`
  height: 100%;
  position: relative;
  display: flex;
  flex-direction: column;
`;

const ScrollableContainer = styled.ul`
  position: relative;
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

  @media ${MEDIA_QUERY.MOBILE} {
    margin-bottom: 1.5rem;
  }
`;

const MoreButton = styled.button`
  bottom: 0.25rem;
  width: calc(100% - 0.5rem);
  height: 1.25rem;
  margin: 0 0.5rem;
  border: none;
  border-radius: 0.25rem;
  background-color: ${PALETTE.WHITE_400};
  filter: drop-shadow(0 0 0.25rem rgba(0, 0, 0, 0.25));
  overflow: hidden;

  ${hoverLayer({})};
`;

export const MoreFeedsArrow = styled(DownPolygon)`
  fill: ${PALETTE.PRIMARY_400};
`;

export default {
  Root,
  CardsContainer,
  ScrollableContainer,
  VerticalAvatar,
  LevelButtonsContainer,
  MoreButton,
};
