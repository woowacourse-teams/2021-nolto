import useMember from 'hooks/queries/useMember';
import { customRenderHook } from 'test-util';
import { MOCK_USER } from '__mocks__/fixture/user';

describe('useMember 테스트', () => {
  beforeEach(() => {
    localStorage.setItem('accessToken', 'token');
  });

  it('유저 정보를 가져올 수 있다.', async () => {
    const { result, waitForNextUpdate } = customRenderHook(() => useMember());

    await waitForNextUpdate();

    expect(result.current.userData).toStrictEqual(MOCK_USER.MAZZI);
  });

  it('로그인되어 있는 유저의 경우 로그인 여부를 가져올 수 있다.', async () => {
    const { result, waitForNextUpdate } = customRenderHook(() => useMember());

    await waitForNextUpdate();

    expect(result.current.isLogin).toBe(true);
  });

  it('로그아웃 할 수 있다.', async () => {
    const { result, waitForNextUpdate } = customRenderHook(() => useMember());

    result.current.logout();

    await waitForNextUpdate();

    expect(result.current.isLogin).toBe(false);
  });
});
