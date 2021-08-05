import styled from 'styled-components';

import { PALETTE } from 'constants/palette';
import ArrowIcon from '../../assets/carouselArrow.svg';

const Reply = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin: 1rem 0 0 2rem;
`;

export const FoldButton = styled.button`
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 0.5rem;
  width: 8rem;
  color: ${PALETTE.PRIMARY_400};
  font-weight: 600;
  background: none;
  border: none;
  font-size: 14px;
`;

const ArrowUp = styled(ArrowIcon)<{ isFold: boolean }>`
  transform: ${({ isFold }) => (isFold ? 'rotate(90deg)' : 'rotate(-90deg)')};

  transition: all 0.2s ease;
`;

const ReplyWrapper = styled.div<{ isFold: boolean; replyCount: number }>`
  display: flex;
  flex-direction: column;
  gap: 1rem;
  overflow: hidden;
  transition: height 0.2s ease;

  height: ${({ isFold, replyCount }) => (isFold ? '0' : `calc(7.5rem * ${replyCount})`)};
`;

export default { Reply, ArrowUp, ReplyWrapper };
