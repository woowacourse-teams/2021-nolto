import styled from 'styled-components';

import DownPolygon from 'assets/downPolygon.svg';
import { MEDIA_QUERY } from 'constants/mediaQuery';
import { PALETTE } from 'constants/palette';

const Root = styled.div`
  display: flex;
  flex-direction: column;
  gap: 3rem;
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

const RecentFeedsContainer = styled.div`
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  grid-gap: 1rem;

  @media ${MEDIA_QUERY.TABLET} {
    grid-template-columns: repeat(4, 1fr);
  }

  @media ${MEDIA_QUERY.TABLET_SMALL} {
    grid-template-columns: repeat(2, 1fr);
  }
`;

export const MoreFeedsArrow = styled(DownPolygon)`
  fill: ${PALETTE.PRIMARY_400};
`;

const MoreHiddenElement = styled.div`
  width: 100%;
  height: 20px;
  visibility: hidden;
`;

export default {
  Root,
  StepChipsContainer,
  Button,
  RecentFeedsContainer,
  MoreHiddenElement,
};
