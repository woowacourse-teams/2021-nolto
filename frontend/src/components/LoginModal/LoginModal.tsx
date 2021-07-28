import React from 'react';

import GithubLogo from 'assets/githubLogo.svg';
import GoogleLogo from 'assets/googleLogo.svg';
import { ButtonStyle } from 'types';
import api from 'constants/api';
import Styled, { OAuthButton } from './LoginModal.styles';

const LoginModal = () => {
  const githubLogin = async () => {
    const { data } = await api.get('/login/oauth/github');
    const url = 'https://github.com/login/oauth/authorize?' + new URLSearchParams(data);

    window.location.replace(url);
  };

  const googleLogin = async () => {
    const { data } = await api.get('/login/oauth/google');
    const url = 'https://accounts.google.com/o/oauth2/v2/auth?' + new URLSearchParams(data);

    window.location.replace(url);
  };

  return (
    <Styled.Root>
      <Styled.Title>로그인</Styled.Title>
      <Styled.Greeting>👋 놀토에 오신 것을 환영합니다</Styled.Greeting>
      <Styled.OAuthContainer>
        <OAuthButton type="button" buttonStyle={ButtonStyle.OUTLINE} onClick={githubLogin}>
          <GithubLogo width="1.25rem" />
          Github 계정으로 로그인하기
        </OAuthButton>

        <OAuthButton type="button" buttonStyle={ButtonStyle.OUTLINE} onClick={googleLogin}>
          <GoogleLogo width="1.25rem" />
          Google 계정으로 로그인하기
        </OAuthButton>
      </Styled.OAuthContainer>
    </Styled.Root>
  );
};

export default LoginModal;
