import DownPolygon from 'assets/downPolygon.svg';
import { PALETTE } from 'constants/palette';
import styled from 'styled-components';

const Root = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1rem;
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
  margin-top: 4rem;
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  grid-gap: 1rem;
`;

export const MoreFeedsArrow = styled(DownPolygon)`
  fill: ${PALETTE.PRIMARY_400};
`;

export default {
  Root,
  StepChipsContainer,
  Button,
  RecentFeedsContainer,
};
