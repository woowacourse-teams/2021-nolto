import styled from 'styled-components';

import { PALETTE } from 'constants/palette';
import ArrowIcon from 'assets/carouselArrow.svg';
import IconButton from 'components/@common/IconButton/IconButton';

const Root = styled.div`
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  padding: 1rem 0 0 2rem;
`;

export const ReplyIconButton = styled(IconButton)`
  position: absolute;
  top: -1.15rem;
  left: 6.25rem;
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

const SubCommentWrapper = styled.div<{
  isFold: boolean;
  isReplyFormVisible: boolean;
  replyCount: number;
}>`
  display: flex;
  flex-direction: column;
  gap: 1rem;
  overflow: hidden;
  transition: max-height ${({ isFold }) => (isFold ? '0.35s ease' : '0.85s ease')};

  max-height: ${({ isFold, isReplyFormVisible, replyCount }) => {
    if (isFold) return 0;

    let height = 0;

    if (isReplyFormVisible) height += 30;

    return `${replyCount * 50 + height}rem`;
  }};
`;

export default { Root, ArrowUp, SubCommentWrapper };
