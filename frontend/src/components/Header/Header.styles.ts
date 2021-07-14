import styled from 'styled-components';

import { PALETTE } from 'constants/palette';
import TextButton from 'components/@common/TextButton/TextButton';
import IconButton from 'components/@common/IconButton/IconButton';

const Root = styled.header`
  position: sticky;
  top: 0;
  height: 108px;
  width: 100%;
  z-index: 10;
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
    font-size: 24px;
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
  margin-left: 40px;
  gap: 28px;
`;

const SignInButton = styled(TextButton.Rounded)`
  padding: 0.5rem 2rem;
  font-size: 1.5rem;
  line-height: 1.5rem;
`;

const SearchButton = styled(IconButton)`
  width: 2.75rem;
  height: 2.75rem;
  padding: 0.5rem;
`;

export default {
  Root,
  HeaderContent,
  LogoWrapper,
  NavContainer,
  ButtonsContainer,
  SignInButton,
  SearchButton,
};
