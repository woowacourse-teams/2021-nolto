import styled from 'styled-components';

import Avatar from 'components/@common/Avatar/Avatar';
import { PALETTE } from 'constants/palette';
import DownPolygon from 'assets/downPolygon.svg';
import { hoverLayer } from 'commonStyles';

const Root = styled.div`
  display: flex;
  flex-direction: column;
  height: 100%;
`;

const ScrollableContainer = styled.ul`
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  grid-gap: 1rem;

  &::-webkit-scrollbar {
    display: none;
  }

  /* Hide scrollbar for IE, Edge and Firefox */
  -ms-overflow-style: none;
  scrollbar-width: none;

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
  ScrollableContainer,
  VerticalAvatar,
  MoreButton,
  MoreFeedsArrow,
};
