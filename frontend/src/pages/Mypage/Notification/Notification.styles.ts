import styled from 'styled-components';

import { PALETTE } from 'constants/palette';
import { MEDIA_QUERY } from 'constants/mediaQuery';
import ArrowIcon from 'assets/carouselArrow.svg';
import { defaultShadow } from 'commonStyles';

const Root = styled.div<{ isFolded: boolean; notiCount: number }>`
  position: relative;
  padding: 1rem 2rem;
  width: 100%;
  border-radius: 0.75rem;
  display: flex;
  flex-direction: column;
  transition: max-height 0.25s ease;
  ${defaultShadow};

  max-height: ${({ isFolded, notiCount }) =>
    isFolded ? '15rem' : `calc(15rem + ${notiCount - 3} * 2.25rem)`};

  @media ${MEDIA_QUERY.TABLET} {
    padding: 1rem;
    max-height: ${({ isFolded, notiCount }) =>
      isFolded ? '18rem' : `calc(18rem + ${notiCount - 3} * 4.5rem)`};
  }

  @media ${MEDIA_QUERY.MOBILE} {
    max-height: ${({ isFolded, notiCount }) =>
      isFolded ? '21rem' : `calc(21rem + ${notiCount - 3} * 6.75rem)`};
  }
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
  flex-basis: 14rem;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 0.25rem;
  background: transparent;
  border: none;
`;

const NotiContainer = styled.ul`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  margin-top: 0.75rem;
  margin-bottom: 1.75rem;
  padding: 0.25rem;
`;

const NotiWrapper = styled.li`
  display: flex;
  align-items: center;
  cursor: pointer;

  &:hover {
    text-decoration: underline;
  }
`;

const NotiUserImage = styled.img`
  width: 1.5rem;
  height: 1.5rem;
  border-radius: 50%;
  margin-right: 0.25rem;
`;

const NotiText = styled.div`
  width: 100%;
`;

const NotiBold = styled.span`
  display: inline-block;
  font-weight: 700;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  vertical-align: middle;

  &.user-name {
    max-width: 3rem;
  }

  &.feed-title {
    max-width: 5.5rem;
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

const NoNotiContent = styled.div`
  display: flex;

  > span {
    margin: auto;
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
  NoNotiContent,
};
