import styled, { css, keyframes } from 'styled-components';

import { Card } from 'commonStyles';
import { PALETTE } from 'constants/palette';
import { MEDIA_QUERY } from 'constants/mediaQuery';

const loading = keyframes`
  0% {
    transform: translateX(0);
  }

  50%,
  100% {
    transform: translateX(460px);
  }
`;

const gradient = css`
  overflow: hidden;
  position: relative;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 8rem;
    height: 100%;
    background: ${`linear-gradient(to right, ${PALETTE.GRAY_100}, ${PALETTE.GRAY_200}, ${PALETTE.GRAY_100})`};
    animation: ${loading} 2s infinite linear;
  }
`;

const Root = styled.div`
  position: relative;
  width: 100%;
  min-width: 14rem;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  gap: 0.5rem;
`;

const AvatarContainer = styled.div`
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
  width: 100%;
`;

const UserImage = styled.span`
  width: 1.5rem;
  height: 1.5rem;
  border-radius: 50%;
  background-color: ${PALETTE.GRAY_100};
  ${gradient};
`;

const Nickname = styled.span`
  width: 5rem;
  height: 1rem;
  border-radius: 4px;
  background-color: ${PALETTE.GRAY_100};
  ${gradient};
`;

const FeedContainer = styled(Card)`
  padding-top: 125%;
  overflow: hidden;
  position: relative;
  border: 2px solid ${PALETTE.GRAY_100};
  box-shadow: none;

  @media ${MEDIA_QUERY.TABLET} {
    width: 12.5rem;
    height: 15.75rem;
  }

  @media ${MEDIA_QUERY.MOBILE} {
    width: 9.75rem;
    height: 12rem;
  }
`;

const Image = styled.div`
  ${gradient};
  position: absolute;
  top: 0;
  left: 0;
  height: 70%;
  width: 100%;
  background-color: ${PALETTE.GRAY_100};
`;

const ContentWrapper = styled.div`
  position: absolute;
  bottom: 0;
  width: 100%;
  height: 30%;
  padding: 0.75rem 1rem;

  @media ${MEDIA_QUERY.MOBILE} {
    height: 4.5rem;
  }
`;

const Title = styled.h3`
  width: 40%;
  height: 25%;
  margin-bottom: 1.5rem;
  background-color: ${PALETTE.GRAY_100};
  border-radius: 4px;
  ${gradient};
`;

const Content = styled.p`
  width: 100%;
  height: 20%;
  margin: 0.25rem 0;
  background-color: ${PALETTE.GRAY_100};
  border-radius: 4px;
  ${gradient};
`;

export default {
  Root,
  AvatarContainer,
  UserImage,
  Nickname,
  FeedContainer,
  Image,
  ContentWrapper,
  Title,
  Content,
};
