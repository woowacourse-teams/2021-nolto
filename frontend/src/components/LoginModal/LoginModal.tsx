import React from 'react';

import GithubLogo from 'assets/githubLogo.svg';
import GoogleLogo from 'assets/googleLogo.svg';
import { ButtonStyle } from 'types';
import { backendApi } from 'constants/api';
import hasWindow from 'constants/windowDetector';
import Styled, { OAuthButton } from './LoginModal.styles';

const LoginModal = () => {
  const githubLogin = async () => {
    const { data } = await backendApi.get('/login/oauth/github');
    const url = 'https://github.com/login/oauth/authorize?' + new URLSearchParams(data);

    if (hasWindow) {
      window.location.replace(url);
    }
  };

  const googleLogin = async () => {
    const { data } = await backendApi.get('/login/oauth/google');
    const url = 'https://accounts.google.com/o/oauth2/v2/auth?' + new URLSearchParams(data);

    if (hasWindow) {
      window.location.replace(url);
    }
  };

  return (
    <Styled.Root>
      <Styled.Title>๋ก๊ทธ์ธ</Styled.Title>
      <Styled.Greeting>๐ ๋ํ ์ ์ค์  ๊ฒ์ ํ์ํฉ๋๋ค</Styled.Greeting>
      <Styled.OAuthContainer>
        <OAuthButton type="button" buttonStyle={ButtonStyle.OUTLINE} onClick={githubLogin}>
          <GithubLogo width="1.25rem" />
          Github ๊ณ์ ์ผ๋ก ๋ก๊ทธ์ธํ๊ธฐ
        </OAuthButton>

        <OAuthButton type="button" buttonStyle={ButtonStyle.OUTLINE} onClick={googleLogin}>
          <GoogleLogo width="1.25rem" />
          Google ๊ณ์ ์ผ๋ก ๋ก๊ทธ์ธํ๊ธฐ
        </OAuthButton>
      </Styled.OAuthContainer>
    </Styled.Root>
  );
};

export default LoginModal;
