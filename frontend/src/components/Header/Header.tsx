import React, { useState } from 'react';
import { Link, NavLink } from 'react-router-dom';

import Pencil from 'assets/pencil.svg';
import Search from 'assets/search.svg';
import Page from 'pages';
import LoginModal from 'components/LoginModal/LoginModal';
import { PALETTE } from 'constants/palette';
import ROUTE from 'constants/routes';
import useModal from 'contexts/modal/useModal';
import useMember from 'hooks/queries/useMember';
import { ButtonStyle } from 'types';
import Styled, { Logo, LogoText, Searchbar } from './Header.styles';
import IconButton from 'components/@common/IconButton/IconButton';
import UserProfile from 'components/UserProfile/UserProfile';

interface Props {
  isFolded?: boolean;
}

const Header = ({ isFolded = false }: Props) => {
  const modal = useModal();
  const [isSearchBarOpened, setSearchBarOpened] = useState(false);
  const member = useMember();

  const openLoginModal = () => {
    modal.openModal(<LoginModal />);
  };

  const openSearchBar = () => {
    setSearchBarOpened(true);
  };

  const closeSearchBar = (event: React.MouseEvent<HTMLElement>) => {
    if (event.target === event.currentTarget) {
      setSearchBarOpened(false);
    }
  };

  return (
    <Styled.Root isFolded={isFolded} onClick={closeSearchBar}>
      <Styled.BackgroundSvg height="100%" width="100vw">
        <defs>
          <linearGradient id="grad1" x1="0%" y1="0%" x2="100%" y2="0%">
            <stop offset="20%" stopColor={PALETTE.PRIMARY_200} stopOpacity="1" />
            <stop offset="80%" stopColor={PALETTE.PRIMARY_400} stopOpacity="1" />
          </linearGradient>
        </defs>
        <rect x="-30vw" y="0" width="160vw" height="100%" fill="url(#grad1)" />
      </Styled.BackgroundSvg>

      <Styled.LogoWrapper>
        <Link to={ROUTE.HOME}>
          <Logo height="100%" />
          <LogoText height="90%" />
        </Link>
      </Styled.LogoWrapper>
      <Styled.NavContainer>
        <li onMouseOver={() => Page.RecentFeeds.preload()}>
          <NavLink to={ROUTE.RECENT} className="nav-link" activeClassName="selected">
            Toy Projects
          </NavLink>
        </li>
        <li className="web-hosting">
          <a href="https://joel-web-hosting.o-r.kr/" className="nav-link" target="_blank">
            Joel’s Hosting
          </a>
        </li>
        <li>
          <NavLink to={ROUTE.ABOUT} className="nav-link" activeClassName="selected">
            Nolto Team
          </NavLink>
        </li>
        <li className="buttons-container">
          <Styled.ButtonsContainer>
            <Link to={ROUTE.UPLOAD} className="upload-link">
              <IconButton size="2rem">
                <Pencil fill={PALETTE.PRIMARY_400} />
              </IconButton>
            </Link>
            <div>
              <IconButton size="2rem" onClick={openSearchBar} className="search">
                <Search fill={PALETTE.PRIMARY_400} />
              </IconButton>
              {isSearchBarOpened && <Searchbar placeholder="제목/내용으로만 검색이 가능합니다" />}
            </div>
          </Styled.ButtonsContainer>
        </li>
      </Styled.NavContainer>
      <Styled.UserContainer>
        {member.userData ? (
          <UserProfile />
        ) : (
          <Styled.AuthButton
            buttonStyle={ButtonStyle.OUTLINE}
            reverse={true}
            onClick={openLoginModal}
          >
            Sign In
          </Styled.AuthButton>
        )}
      </Styled.UserContainer>
    </Styled.Root>
  );
};

export default Header;
