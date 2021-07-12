import React from 'react';
import { NavLink } from 'react-router-dom';

import Logo from 'assets/logo.svg';
import Search from 'assets/search.svg';
import Styled from './Header.styles';
import { PALETTE } from 'constants/palette';
import IconButton from 'components/@common/IconButton/IconButton';
import { ButtonStyle } from 'types';

const Header = () => {
  const navLinkActiveStyle = {
    borderBottom: `2px solid ${PALETTE.WHITE_400}`,
  };

  return (
    <header>
      <Styled.SvgRoot height="168px" width="100vw">
        <defs>
          <linearGradient id="grad1" x1="0%" y1="0%" x2="100%" y2="0%">
            <stop offset="0%" stopColor={PALETTE.PRIMARY_200} stopOpacity="1" />
            <stop offset="100%" stopColor={PALETTE.PRIMARY_400} stopOpacity="1" />
          </linearGradient>
        </defs>
        <rect x="-30vw" y="0" width="160vw" height="100%" fill="url(#grad1)" />
      </Styled.SvgRoot>

      <Styled.HeaderContent>
        <Styled.LogoWrapper>
          <Logo />
        </Styled.LogoWrapper>
        <nav>
          <Styled.NavContainer>
            <li>
              <NavLink to="/" activeStyle={navLinkActiveStyle}>
                Feed
              </NavLink>
            </li>
            <li>
              <NavLink to="/best" activeStyle={navLinkActiveStyle}>
                Best 10
              </NavLink>
            </li>
            <li>
              <NavLink to="/hosting" activeStyle={navLinkActiveStyle}>
                Joel’s Hosting
              </NavLink>
            </li>
            <li>
              <NavLink to="/makers" activeStyle={navLinkActiveStyle}>
                Toy Makers
              </NavLink>
            </li>
          </Styled.NavContainer>
        </nav>
        <Styled.ButtonsContainer>
          <IconButton>
            <Search />
          </IconButton>
          <Styled.SignInButton buttonStyle={ButtonStyle.OUTLINE} reverse={true}>
            Sign In
          </Styled.SignInButton>
        </Styled.ButtonsContainer>
      </Styled.HeaderContent>
    </header>
  );
};

export default Header;
