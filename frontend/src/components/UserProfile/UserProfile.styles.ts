import styled, { keyframes, css } from 'styled-components';
import { Link as LinkElement } from 'react-router-dom';

import IconButton from 'components/@common/IconButton/IconButton';
import Thumbnail from 'components/Thumbnail/Thumbnail';
import { PALETTE } from 'constants/palette';
import { MEDIA_QUERY } from 'constants/mediaQuery';
import { FONT_SIZE } from 'constants/styles';
import { hoverLayer } from 'commonStyles';

const open = keyframes`
  from {
    opacity: 0;
  }

  to {
    opacity: 1;
  }

`;

const Root = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 0.75rem;
`;

const UserThumbnail = styled.div`
  display: flex;
  align-items: center;
  position: relative;
  cursor: pointer;
`;

const NotiAlert = styled.div`
  position: absolute;
  top: -0.15rem;
  left: -0.35rem;
  width: 1.15rem;
  height: 1.15rem;
  color: ${PALETTE.WHITE_400};
  font-size: 0.65rem;
  line-height: 1.15rem;
  text-align: center;
  background-color: ${PALETTE.RED_400};
  border: 1px solid ${PALETTE.PRIMARY_400};
  border-radius: 50%;

  @media ${MEDIA_QUERY.MOBILE} {
    width: 0.85rem;
    height: 0.85rem;
    font-size: 0.5rem;
    line-height: 0.75rem;
  }
`;

const Image = styled(Thumbnail)`
  width: 2.5rem;
  height: 2.5rem;
  border-radius: 50%;

  @media ${MEDIA_QUERY.MOBILE} {
    width: 2rem;
    height: 2rem;
  }
`;

const MoreProfileButton = styled(IconButton)`
  border: none;
  background: transparent;
`;

const Dropdown = styled.div<{ isOpen: boolean }>`
  position: absolute;
  display: ${({ isOpen }) => (isOpen ? 'flex' : 'none')};
  position: absolute;
  width: fit-content;
  min-width: 6.5rem;
  max-width: 8rem;
  top: 120%;
  flex-direction: column;
  align-items: center;
  border: 1px solid ${PALETTE.PRIMARY_400};
  border-radius: 4px;
  animation: ${open} 0.3s ease;
  overflow: hidden;

  * {
    padding: 0.5rem 0.75rem;
  }
`;

const Greeting = styled.div`
  width: 100%;
  background-color: ${PALETTE.PRIMARY_400};
  color: ${PALETTE.WHITE_400};
  text-align: center;
`;

const buttonStyle = css`
  width: 100%;
  border: none;
  border-top: 1px solid ${PALETTE.PRIMARY_400};
  background-color: ${PALETTE.WHITE_400};
  text-align: center;
  font-size: 1rem;

  &:last-child {
    border-radius: 0 0 4px 4px;
  }

  ${hoverLayer({})};
`;

export const ProfileLink = styled(LinkElement)`
  ${buttonStyle};
`;

export const NotiLink = styled(LinkElement)`
  ${buttonStyle};

  display: flex;
  align-items: center;
  justify-content: space-evenly;

  & .noti-count {
    display: inline-block;
    width: 1.25rem;
    height: 1.25rem;
    border-radius: 50%;
    padding: 0;
    background-color: ${PALETTE.RED_400};
    color: ${PALETTE.WHITE_400};
    font-size: ${FONT_SIZE.SMALL};
    line-height: 1.25rem;
    text-align: center;
  }
`;

const Button = styled.button`
  ${buttonStyle};
`;

export default {
  Root,
  UserThumbnail,
  NotiAlert,
  Image,
  MoreProfileButton,
  Dropdown,
  Greeting,
  Button,
};
