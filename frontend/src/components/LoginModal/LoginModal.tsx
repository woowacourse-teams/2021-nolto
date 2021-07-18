import React from 'react';
import { useHistory } from 'react-router-dom';
import { useForm } from 'react-hook-form';

import GithubLogo from 'assets/githubLogo.svg';
import GoogleLogo from 'assets/googleLogo.svg';
import { ButtonStyle, LoginInfo } from 'types';
import useModal from 'hooks/@common/useModal';
import useUserInfo from 'hooks/@common/useUserInfo';
import useLogin from 'hooks/queries/useLogin';
import REGEX from 'constants/regex';
import ROUTE from 'constants/routes';
import ErrorMessage from 'components/@common/ErrorMessage/ErrorMessage';
import Styled, { Form, LoginInput, OAuthButton } from './LoginModal.styles';

const LoginModal = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginInfo>();
  const loginMutation = useLogin();
  const history = useHistory();
  const modal = useModal();
  const userInfo = useUserInfo();

  const login = (loginData: LoginInfo) => {
    loginMutation.mutate(loginData, {
      onSuccess: ({ data }, variables) => {
        localStorage.setItem('accessToken', data.accessToken);
        userInfo.setUserInfo({ email: variables.email });

        modal.closeModal();
        history.push(ROUTE.HOME);
      },
    });
  };

  return (
    <Styled.Root>
      <Styled.Title>로그인</Styled.Title>
      <Form onSubmit={handleSubmit(login)}>
        <Styled.InputWrapper>
          <LoginInput
            placeholder="email"
            {...register('email', {
              required: '💕 이메일을 입력해주세요',
              pattern: {
                value: REGEX.EMAIL,
                message: '💌 이메일 형식으로 입력해주세요',
              },
            })}
          />
          <ErrorMessage targetError={errors.email} />
        </Styled.InputWrapper>

        <Styled.InputWrapper>
          <LoginInput
            type="password"
            placeholder="password"
            {...register('password', { required: '🔑 패스워드를 입력해주세요' })}
          />
          <ErrorMessage targetError={errors.password} />
        </Styled.InputWrapper>

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
      </Form>

      <Styled.SignUpWrapper>
        <span>🤔 아직 회원이 아니신가요?</span>
        <Styled.SignUpLink to="/">&nbsp; 회원가입</Styled.SignUpLink>
      </Styled.SignUpWrapper>
    </Styled.Root>
  );
};

export default LoginModal;
