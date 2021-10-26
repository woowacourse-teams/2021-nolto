import React, { useState } from 'react';
import { Link, NavLink } from 'react-router-dom';

import Pencil from 'assets/pencil.svg';
import Search from 'assets/search.svg';
import SignIn from 'assets/signin.svg';
import LoginModal from 'components/LoginModal/LoginModal';
import IconButton from 'components/@common/IconButton/IconButton';
import UserProfile from 'components/UserProfile/UserProfile';
import { PALETTE } from 'constants/palette';
import ROUTE from 'constants/routes';
import useFocusOut from 'hooks/@common/useFocusOut';
import useModal from 'contexts/modal/useModal';
import useMember from 'contexts/member/useMember';
import Page from 'pages';
import { ButtonStyle } from 'types';
import Styled, { Logo, LogoText, Searchbar } from './Header.styles';

interface Props {
  isFolded?: boolean;
}

const Header = ({ isFolded = false }: Props) => {
  const modal = useModal();
  const [isSearchbarOpened, setSearchbarOpened] = useState(false);
  const member = useMember();
  const searchbarRef = useFocusOut(() => setSearchbarOpened(false));

  const openLoginModal = () => {
    modal.openModal(<LoginModal />);
  };

  const openSearchbar = () => {
    setSearchbarOpened(true);
  };

  const closeSearchbar = (event: React.MouseEvent<HTMLElement>) => {
    if (event.target === event.currentTarget) {
      setSearchbarOpened(false);
    }
  };

  return (
    <Styled.Root isFolded={isFolded} onClick={closeSearchbar}>
      <Styled.BackgroundSvg height="100%" width="100vw">
        <defs>
          <linearGradient id="grad1" x1="0%" y1="0%" x2="100%" y2="0%">
            <stop offset="20%" stopOpacity="1" />
            <stop offset="80%" stopOpacity="1" />
          </linearGradient>
        </defs>
        <rect x="-30vw" y="0" width="160vw" height="100%" fill="url(#grad1)" />
      </Styled.BackgroundSvg>

      <Styled.LogoWrapper>
        <Link to={ROUTE.HOME} aria-label="홈">
          <Logo height="100%" />
          <LogoText height="90%" />
          <h1 className="visually-hidden">놀토: 놀러오세요 토이프로젝트</h1>
        </Link>
      </Styled.LogoWrapper>
      <Styled.NavContainer>
        <ul>
          <li onMouseOver={() => Page.RecentFeeds.preload()}>
            <NavLink to={ROUTE.RECENT} className="nav-link" activeClassName="selected">
              Toy Projects
            </NavLink>
          </li>
          <li className="web-hosting">
            <a href="https://easy-deploy.kr/" className="nav-link" target="_blank">
              Easy Deploy
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
                <IconButton size="2rem" aria-label="토이 프로젝트 업로드">
                  <Pencil fill={PALETTE.PRIMARY_400} />
                </IconButton>
              </Link>
              <div ref={searchbarRef}>
                <IconButton
                  size="2rem"
                  onClick={openSearchbar}
                  className="search"
                  aria-label="토이 프로젝트 검색"
                >
                  <Search fill={PALETTE.PRIMARY_400} />
                </IconButton>
                {isSearchbarOpened && <Searchbar placeholder="제목/내용으로만 검색이 가능합니다" />}
              </div>
            </Styled.ButtonsContainer>
          </li>
        </ul>
      </Styled.NavContainer>
      <Styled.UserContainer>
        {member.userInfo ? (
          <UserProfile />
        ) : (
          <>
            <Styled.AuthButton
              buttonStyle={ButtonStyle.OUTLINE}
              reverse={true}
              onClick={openLoginModal}
            >
              Sign In
            </Styled.AuthButton>
            <IconButton size="2rem" onClick={openLoginModal} className="signin" aria-label="signin">
              <SignIn fill={PALETTE.PRIMARY_400} />
            </IconButton>
          </>
        )}
      </Styled.UserContainer>
    </Styled.Root>
  );
};

export default Header;
