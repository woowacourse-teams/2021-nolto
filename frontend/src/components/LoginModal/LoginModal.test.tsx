import React from 'react';

import { customRender, fireEvent } from 'test-util';
import LoginModal from './LoginModal';
import Header from '../Header/Header';
import { backendApi } from 'constants/api';

const assignMock = jest.fn();

delete window.location;
(window.location as unknown) = { assign: assignMock };

afterEach(() => {
  assignMock.mockClear();
});

jest.mock('constants/api', () => {
  return {
    backendApi: {
      get: jest.fn(),
      create: jest.fn(() => ({
        interceptors: {
          request: { use: jest.fn() },
          response: { use: jest.fn() },
        },
      })),
    },
  };
});

describe('LoginModal 테스트', () => {
  it('signin 버튼 클릭 시 로그인 모달이 뜬다.', async () => {
    const { getByRole } = customRender(<Header />);
    const signInButton = getByRole('button', { name: /sign in/i });

    fireEvent.click(signInButton);

    expect(getByRole('heading', { name: /로그인/i })).toBeInTheDocument();
  });

  it('github 계정으로 로그인 시 github oauth에서 정보를 받아온다.', async () => {
    const { getByRole } = customRender(<LoginModal />);
    const githubButton = getByRole('button', { name: /github 계정으로 로그인하기/i });

    fireEvent.click(githubButton);

    (backendApi.get as jest.Mock).mockResolvedValueOnce({});
    expect(backendApi.get).toHaveBeenCalledWith('/login/oauth/github');
  });

  it('google 계정으로 로그인 시 google oauth에서 정보를 받아온다.', async () => {
    const { getByRole } = customRender(<LoginModal />);
    const googleButton = getByRole('button', { name: /google 계정으로 로그인하기/i });

    fireEvent.click(googleButton);

    (backendApi.get as jest.Mock).mockResolvedValueOnce({});
    expect(backendApi.get).toHaveBeenCalledWith('/login/oauth/google');
  });
});
