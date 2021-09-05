import styled, { css, keyframes } from 'styled-components';

import { PALETTE } from 'constants/palette';

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
  display: flex;
  position: relative;
  flex-direction: column;
  min-width: 10rem;
  width: 100%;
  gap: 0.5rem;

  & .project-image {
    transition: all 0.2s ease;

    &:hover {
      transform: scale(1.05);
    }
  }
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

const RegularCardImgWrapper = styled.div`
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 7px;
  padding-top: 100%;
  overflow: hidden;
  background-color: rgba(0, 0, 0, 0.1);

  & > img {
    position: absolute;
    left: 0;
    top: 0;
    height: 100%;
    width: 100%;
  }
`;

const Image = styled.div`
  ${gradient};
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  background-color: ${PALETTE.GRAY_100};
`;

const ContentWrapper = styled.div`
  display: flex;
  flex-direction: column;
  height: 6rem;
  padding: 0.25rem;
  gap: 0.25rem;

  & > h3 {
    font-size: 16px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }

  & > p {
    overflow: hidden;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
    font-size: 14px;
  }
`;

const Title = styled.h3`
  width: 40%;
  height: 20%;
  margin-bottom: 0.5rem;
  background-color: ${PALETTE.GRAY_100};
  border-radius: 4px;
  ${gradient};
`;

const Content = styled.p`
  width: 100%;
  height: 20%;
  margin: 0.1rem 0;
  background-color: ${PALETTE.GRAY_100};
  border-radius: 4px;
  ${gradient};
`;

export default {
  Root,
  AvatarContainer,
  UserImage,
  Nickname,
  RegularCardImgWrapper,
  Image,
  ContentWrapper,
  Title,
  Content,
};
