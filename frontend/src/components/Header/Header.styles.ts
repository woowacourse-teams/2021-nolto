import styled from 'styled-components';

import { PALETTE } from 'constants/palette';
import TextButton from 'components/@common/TextButton/TextButton';

const SvgRoot = styled.svg`
  position: absolute;
  top: 0;
  z-index: 10;
`;

const HeaderContent = styled.div`
  position: fixed;
  top: 0;
  display: flex;
  width: 100%;
  justify-content: flex-end;
  align-items: center;
  padding: 36px 60px;
  z-index: 20;
`;

const LogoWrapper = styled.div`
  flex: 1;
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
  width: 160px;
  height: 48px;
  padding: 12px 44px;
  font-size: 24px;
  line-height: 24px;
`;

export default {
  SvgRoot,
  HeaderContent,
  LogoWrapper,
  NavContainer,
  ButtonsContainer,
  SignInButton,
};
