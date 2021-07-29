import React from 'react';
import { QueryClient } from 'react-query';
import { renderHook, act } from '@testing-library/react-hooks';

import useOAuthLogin from 'src/hooks/useOAuthLogin';

describe('백엔드 서버로부터 OAuth 로그인 정보에 대한 accessToken을 받아온다.', () => {
  it('깃헙으로 부터 액세스 토큰을 받아올 수 있다.', () => {
    const { result } = renderHook(() => useOAuthLogin('github'));

    expect(localStorage.getItem('accessToken')).not.toBe(undefined);
  });
});
