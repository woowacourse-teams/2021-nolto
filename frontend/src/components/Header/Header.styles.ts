import styled, { keyframes } from 'styled-components';

import { PALETTE } from 'constants/palette';
import TextButton from 'components/@common/TextButton/TextButton';
import IconButtonComponent from 'components/@common/IconButton/IconButton';
import SearchBarComponent from 'components/SearchBar/SearchBar';
import Z_INDEX from 'constants/zIndex';
import UserProfileComponent from 'components/UserProfile/UserProfile';

const Root = styled.header<{ isFolded: boolean }>`
  position: sticky;
  top: 0;
  height: 92px;
  width: 100%;
  z-index: ${Z_INDEX.HEADER};
  box-shadow: ${({ isFolded }) => !isFolded && '0px 4px 4px rgba(0, 0, 0, 0.25)'};

  & svg {
    width: 100%;
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
  padding: 0 30px;
`;

const LogoWrapper = styled.div`
  width: 208px;
  height: auto;
  margin-right: auto;
  cursor: pointer;
`;

const NavContainer = styled.ul`
  display: flex;
  gap: 36px;

  & a {
    font-size: 20px;
    color: ${PALETTE.WHITE_400};
    display: inline;

    &:hover {
      border-bottom: 2px solid ${PALETTE.WHITE_400};
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
  gap: 1rem;
`;

const AuthButton = styled(TextButton.Rounded)`
  padding: 8px 32px;
  font-size: 20px;
  line-height: 20px;
`;

export const IconButton = styled(IconButtonComponent)`
  width: 2.5rem;
  height: 2.5rem;
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
