import React from 'react';
import { QueryClient } from 'react-query';
import { screen, render, cleanup, fireEvent } from '@testing-library/react';

import LoginModal from 'components/LoginModal/LoginModal';

jest.mock('../../src/constants/api');

describe('로그인 기능', () => {
  const queryClient = new QueryClient();

  beforeAll(() => {
    render(<LoginModal />);
  });

  it('github으로 로그인하기 버튼을 눌러 github OAuth 링크로 이동한다.', () => {
    // const wrapper = ({ children }) => (
    //   <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
    // );
    // const { result, waitFor } = renderHook(() => useCustomHook(), { wrapper });
    // await waitFor(() => result.current.isSuccess);
    // expect(result.current.data).toEqual('Hello');
  });

  it('google로 로그인하기 버튼을 눌러 google 로그인을 할 수 있다.', () => {});

  afterAll(cleanup);
});
