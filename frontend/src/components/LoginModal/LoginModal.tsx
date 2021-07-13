import React from 'react';

import TextButton from 'components/@common/TextButton/TextButton';
import TextInput from 'components/@common/TextInput/TextInput';
import { ButtonStyle } from 'types';
import Styled, { LoginInput, OAuthButton } from './LoginModal.styles';
import GithubLogo from 'assets/githubLogo.svg';
import GoogleLogo from 'assets/googleLogo.svg';

const LoginModal = () => {
  return (
    <Styled.Root>
      <Styled.Title>로그인</Styled.Title>
      <LoginInput placeholder="email" />
      <LoginInput placeholder="password" />

      <Styled.OAuthContainer>
        <OAuthButton buttonStyle={ButtonStyle.OUTLINE}>
          <GithubLogo width="1.25rem" />
          Github 계정으로 로그인하기
        </OAuthButton>

        <OAuthButton buttonStyle={ButtonStyle.OUTLINE}>
          <GoogleLogo width="1.25rem" />
          Google 계정으로 로그인하기
        </OAuthButton>
      </Styled.OAuthContainer>

      <Styled.LoginButton buttonStyle={ButtonStyle.SOLID}>Login</Styled.LoginButton>

      <Styled.SignUpWrapper>
        <span>🤔 아직 회원이 아니신가요?</span>
        <Styled.SignUpLink to="/">회원가입</Styled.SignUpLink>
      </Styled.SignUpWrapper>
    </Styled.Root>
  );
};

export default LoginModal;
