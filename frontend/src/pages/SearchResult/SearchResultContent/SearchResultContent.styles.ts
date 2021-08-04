import styled from 'styled-components';

import Avatar from 'components/@common/Avatar/Avatar';
import { PALETTE } from 'constants/palette';
import DownPolygon from 'assets/downPolygon.svg';

const Root = styled.div`
  display: flex;
  flex-direction: column;
  height: 100%;
`;

const ScrollableContainer = styled.ul`
  display: flex;
  flex-direction: column;
  gap: 2rem;
  align-items: center;
  height: 100%;
  padding: 0 1rem;
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

  &:hover::after {
    content: '';
    display: block;
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    opacity: 0.1;
    background-color: ${PALETTE.BLACK_400};
  }
`;

export const MoreFeedsArrow = styled(DownPolygon)`
  fill: ${PALETTE.PRIMARY_400};
`;

export default {
  Root,
  ScrollableContainer,
  VerticalAvatar,
  MoreButton,
  MoreFeedsArrow,
};
