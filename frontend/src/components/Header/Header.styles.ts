import styled, { keyframes } from 'styled-components';

import LogoIcon from 'assets/logo.svg';
import LogoSimpleIcon from 'assets/logoSimple.svg';
import { hoverUnderline } from 'commonStyles';
import TextButton from 'components/@common/TextButton/TextButton';
import IconButtonComponent from 'components/@common/IconButton/IconButton';
import SearchBarComponent from 'components/SearchBar/SearchBar';
import UserProfileComponent from 'components/UserProfile/UserProfile';
import { HEIGHT } from 'constants/common';
import { Z_INDEX } from 'constants/styles';
import { MEDIA_QUERY } from 'constants/mediaQuery';
import { PALETTE } from 'constants/palette';

const Root = styled.header<{ isFolded: boolean }>`
  position: fixed;
  top: 0;
  height: ${HEIGHT.HEADER};
  width: 100%;
  z-index: ${Z_INDEX.HEADER};
  box-shadow: ${({ isFolded }) => !isFolded && '0px 4px 4px rgba(0, 0, 0, 0.25)'};

  & svg {
    width: 100%;
  }

  @media ${MEDIA_QUERY.MOBILE} {
    height: 64px;
  }
`;

const HeaderContent = styled.div`
  position: absolute;
  top: 0;
  display: flex;
  width: 100%;
  height: 100%;
  justify-content: flex-end;
  align-items: center;
  padding: 10px 30px;

  @media ${MEDIA_QUERY.MOBILE} {
    padding: 0 18px;
  }
`;

const LogoWrapper = styled.div`
  width: auto;
  height: 100%;
  margin-right: auto;
  cursor: pointer;
  flex-shrink: 0;

  & .logo-simple {
    display: none;
  }

  @media ${MEDIA_QUERY.TABLET} {
    width: 168px;
  }

  @media ${MEDIA_QUERY.MOBILE} {
    width: 32px;
  }
`;

export const Logo = styled(LogoIcon)`
  @media ${MEDIA_QUERY.MOBILE} {
    display: none;
  }
`;

export const LogoSimple = styled(LogoSimpleIcon)`
  @media screen and (min-width: 376px) {
    display: none;
  }
`;

const NavContainer = styled.ul`
  display: flex;
  gap: 36px;

  @media ${MEDIA_QUERY.TABLET} {
    gap: 18px;
  }

  @media ${MEDIA_QUERY.MOBILE} {
    gap: 12px;

    & .web-hosting {
      display: none;
    }
  }

  & a {
    font-size: 18px;
    color: ${PALETTE.WHITE_400};
    display: inline;
    ${hoverUnderline};

    @media ${MEDIA_QUERY.TABLET} {
      font-size: 1rem;
    }

    @media ${MEDIA_QUERY.MOBILE} {
      font-size: 0.85rem;
    }
  }

  & .nav-link {
    border-bottom: 2px solid ${PALETTE.WHITE_400};
  }
`;

const ButtonsContainer = styled.div`
  display: flex;
  align-items: center;
  position: relative;
  margin-left: 40px;
  padding: 8px 0;
  height: 100%;
  gap: 1rem;

  @media ${MEDIA_QUERY.TABLET} {
    margin-left: 1rem;
    gap: 0.75rem;
  }

  @media ${MEDIA_QUERY.MOBILE} {
    margin-left: 0;

    & .search,
    .upload {
      display: none;
    }
  }
`;

const AuthButton = styled(TextButton.Rounded)`
  padding: 4px 16px;
  font-size: inherit;
  line-height: inherit;
  flex-shrink: 0;

  @media ${MEDIA_QUERY.TABLET} {
    padding: 3px 14px;
  }

  @media ${MEDIA_QUERY.MOBILE} {
    padding: 2px 12px;
  }
`;

export const IconButton = styled(IconButtonComponent)`
  width: 2rem;
  height: 2rem;

  @media ${MEDIA_QUERY.TABLET} {
    width: 1.5rem;
    height: 1.5rem;
  }

  @media ${MEDIA_QUERY.MOBILE} {
    width: 1rem;
    height: 1rem;
  }
`;

const stretch = keyframes`
  from {
    width: 2.5rem;
  }
  to {
    width: 35rem;
  }
`;

const fadeIn = keyframes`
  from {
    width: 0;
  }
  to {
    width: 100%;
  }
`;

export const SearchBar = styled(SearchBarComponent)`
  position: absolute;
  transform: translateX(calc(-100% + 2.5rem));
  height: 2.5rem;
  animation: ${stretch} 0.5s ease 0s 1 normal forwards;

  > input {
    animation: ${fadeIn} 0.5s ease 0s 1 normal forwards;
  }

  @media ${MEDIA_QUERY.TABLET} {
    transform: translateX(calc(-100% + 2.25rem));
    width: 30rem;
    height: 2.25rem;
  }

  @media ${MEDIA_QUERY.MOBILE} {
    display: none;
  }
`;

export const UserProfile = styled(UserProfileComponent)`
  margin-left: 1.5rem;
`;

export default {
  Root,
  HeaderContent,
  LogoWrapper,
  NavContainer,
  ButtonsContainer,
  AuthButton,
};
