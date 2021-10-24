import styled, { keyframes } from 'styled-components';

import LogoIcon from 'assets/logo.svg';
import LogoTextIcon from 'assets/logoText.svg';
import { hoverUnderline } from 'commonStyles';
import TextButton from 'components/@common/TextButton/TextButton';
import SearchbarComponent from 'components/Searchbar/Searchbar';
import { FONT_SIZE, Z_INDEX } from 'constants/styles';
import { MEDIA_QUERY } from 'constants/mediaQuery';
import { HEIGHT } from 'constants/common';

const Root = styled.header<{ isFolded: boolean }>`
  position: fixed;
  display: flex;
  align-items: center;
  top: 0;
  height: ${HEIGHT.HEADER};
  width: 100%;
  padding: 0 1rem;
  z-index: ${Z_INDEX.HEADER};
  box-shadow: ${({ isFolded }) => !isFolded && 'rgb(0 0 0 / 25%) 0px 4px 4px'};
`;

const BackgroundSvg = styled.svg`
  position: absolute;
  z-index: -1;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;

  & stop:nth-of-type(1) {
    stop-color: ${({ theme }) => theme.headerStartColor};
  }

  & stop:nth-of-type(2) {
    stop-color: ${({ theme }) => theme.headerEndColor};
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
`;

const LogoWrapper = styled.div`
  height: 100%;
  cursor: pointer;
  flex-shrink: 0;
  padding: 0.5rem;

  & > a {
    height: 100%;
    display: flex;
    align-items: center;
    gap: 0.5rem;
  }

  @media ${MEDIA_QUERY.MOBILE} {
    width: 40px;
    height: 40px;
    padding: 0;
  }
`;

export const Logo = styled(LogoIcon)``;

export const LogoText = styled(LogoTextIcon)`
  @media ${MEDIA_QUERY.MOBILE} {
    display: none;
  }
`;

const NavContainer = styled.nav`
  position: relative;
  width: 100%;

  & ul {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    gap: 1rem;
  }

  & li {
    text-align: center;

    &.buttons-container {
      margin-left: 0.5rem;
    }
  }

  & .nav-link {
    font-size: ${FONT_SIZE.MEDIUM};
    color: ${({ theme }) => theme.highLightedText};
    display: block;
    ${hoverUnderline};

    &.selected::after {
      transform: scaleX(1);
    }

    @media ${MEDIA_QUERY.TABLET} {
      font-size: ${FONT_SIZE.SMALL};
    }
  }

  @media ${MEDIA_QUERY.TABLET} {
    & .upload-link,
    .web-hosting {
      display: none;
    }
  }

  @media ${MEDIA_QUERY.MOBILE} {
    gap: 0.5rem;

    & .web-hosting {
      display: none;
    }
  }
`;

const ButtonsContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: flex-end;
  height: 100%;
  gap: 0.5rem;

  @media ${MEDIA_QUERY.TABLET} {
    gap: 0.75rem;
  }
`;

const AuthButton = styled(TextButton.Rounded)`
  color: ${({ theme }) => theme.highLightedText};
  border-color: ${({ theme }) => theme.highLightedText};
  padding: 4px 16px;
  font-size: inherit;
  line-height: inherit;

  @media ${MEDIA_QUERY.TABLET_SMALL} {
    display: none;
  }
`;

const stretchTo = (maxWidth: string) => keyframes`
  from {
    width: 2.5rem;
  }
  to {
    width: ${maxWidth};
  }
`;

export const Searchbar = styled(SearchbarComponent)`
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  right: 0;
  height: 2.5rem;
  animation: ${stretchTo('100%')} 1s ease 0s 1 normal forwards;
  max-width: 32rem;
`;

const UserContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  flex-shrink: 0;
  margin-left: 1rem;

  @media ${MEDIA_QUERY.TABLET_SMALL} {
    margin-left: 0.5rem;
  }

  & .signin {
    display: none;

    @media ${MEDIA_QUERY.TABLET_SMALL} {
      display: inline-block;
    }
  }
`;

export default {
  Root,
  BackgroundSvg,
  HeaderContent,
  LogoWrapper,
  NavContainer,
  ButtonsContainer,
  AuthButton,
  UserContainer,
};
