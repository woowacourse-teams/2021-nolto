import styled from 'styled-components';

import { PALETTE } from 'constants/palette';
import ArrowIcon from 'assets/carouselArrow.svg';

const Root = styled.div<{ isFolded: boolean; notiCount: number }>`
  position: relative;
  padding: 1rem 2rem;
  width: 32rem;
  height: ${({ isFolded, notiCount }) =>
    isFolded ? '12rem' : `calc(12rem + ${notiCount - 3} * 1rem)`};
  border-radius: 0.75rem;
  box-shadow: 4px 4px 8px 4px rgba(85, 85, 85, 0.2);
  display: flex;
  flex-direction: column;
  transition: height 0.3s ease;
`;

const TopContainer = styled.div`
  display: flex;
  align-items: center;
  width: 100%;
  padding-bottom: 0.4rem;
  border-bottom: 1px solid ${PALETTE.ORANGE_400};
`;

const TitleWrapper = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
  gap: 0.25rem;
`;

const Title = styled.span`
  color: ${PALETTE.ORANGE_400};
`;

const NotiMark = styled.div`
  width: 1.25rem;
  height: 1.25rem;
  font-size: 14px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: ${PALETTE.RED_400};
  border-radius: 50%;
  color: ${PALETTE.WHITE_400};
`;

const AllReadButton = styled.button`
  flex-basis: 10rem;
  display: flex;
  align-items: center;
  gap: 0.25rem;
  background: transparent;
  border: none;
`;

const NotiContainer = styled.ul`
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
  margin-top: 0.5rem;
  padding: 0.25rem;
`;

const NotiWrapper = styled.li`
  display: flex;
  align-items: center;
`;

const NotiUserImage = styled.img`
  width: 1.5rem;
  height: 1.5rem;
  border-radius: 50%;
  margin-right: 0.25rem;
`;

const NotiText = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
`;

const NotiBold = styled.span`
  display: inline-block;
  font-weight: 700;

  &.feed-title {
    width: 5.5rem;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
`;

const DeleteNotiButton = styled.button`
  background: transparent;
  border: none;
  justify-self: flex-end;
`;

export const MoreNotiIcon = styled(ArrowIcon)<{ isFolded: boolean }>`
  transform: ${({ isFolded }) => (isFolded ? 'rotate(90deg)' : 'rotate(-90deg)')};
`;

const MoreNotiButton = styled.button`
  position: absolute;
  bottom: 0.5rem;
  align-self: center;
  background: transparent;
  border: none;
  color: ${PALETTE.ORANGE_400};
  display: flex;
  align-items: center;
  gap: 0.5rem;

  span {
    color: inherit;
  }
`;

export default {
  Root,
  TopContainer,
  TitleWrapper,
  Title,
  NotiMark,
  AllReadButton,
  NotiContainer,
  NotiWrapper,
  NotiUserImage,
  NotiText,
  NotiBold,
  DeleteNotiButton,
  MoreNotiButton,
};
