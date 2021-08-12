import styled from 'styled-components';

import Avatar from 'components/@common/Avatar/Avatar';
import DownPolygon from 'assets/downPolygon.svg';
import { MEDIA_QUERY } from 'constants/mediaQuery';
import { PALETTE } from 'constants/palette';
import { hoverLayer } from 'commonStyles';

const Root = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100%;
`;

const TopContainer = styled.div`
  position: fixed;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2rem;
  background: ${PALETTE.WHITE_400};
  width: 100%;
  padding-top: 4rem;
  padding-bottom: 2rem;
  z-index: 10;
`;

const LevelButtonsContainer = styled.div`
  display: flex;
  justify-content: center;
  gap: 2rem;
  z-index: 10;

  @media ${MEDIA_QUERY.MOBILE} {
    gap: 1rem;
  }
`;

const CardsContainer = styled.div<{ scrollable: boolean }>`
  padding-top: ${({ scrollable }) => scrollable && '20rem'};
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

  & li {
    margin-bottom: 0.25rem;
  }
`;

const VerticalAvatar = styled(Avatar)`
  margin-bottom: 12px;
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

  ${hoverLayer({})};
`;

export const MoreFeedsArrow = styled(DownPolygon)`
  fill: ${PALETTE.PRIMARY_400};
`;

export default {
  Root,
  TopContainer,
  LevelButtonsContainer,
  CardsContainer,
  ScrollableContainer,
  VerticalAvatar,
  MoreButton,
};
