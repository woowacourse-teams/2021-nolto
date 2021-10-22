import { customRenderHook } from 'test-util';
import useOAuthLogin from 'hooks/useOAuthLogin';

describe('useOAuthLogin 테스트', () => {
  it('github으로부터 액세스 토큰을 받아올 수 있다.', async () => {
    const { waitForNextUpdate } = customRenderHook(() => useOAuthLogin('github'));

    await waitForNextUpdate();

    expect(localStorage.getItem('accessToken')).not.toBeNull();
  });

  it('google로부터 액세스 토큰을 받아올 수 있다.', async () => {
    const { waitForNextUpdate } = customRenderHook(() => useOAuthLogin('google'));

    await waitForNextUpdate();

    expect(localStorage.getItem('accessToken')).not.toBeNull();
  });
});
