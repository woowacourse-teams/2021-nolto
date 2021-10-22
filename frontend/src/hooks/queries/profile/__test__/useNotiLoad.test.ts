import useNotiLoad from 'hooks/queries/profile/useNotiLoad';
import { customRenderHook } from 'test-util';
import { MOCK_NOTI } from '__mocks__/fixture/noti';

describe('useNotiLoad 테스트', () => {
  it('유저 알림을 가져올 수 있다.', async () => {
    localStorage.setItem('accessToken', 'token');

    const { result, waitForNextUpdate } = customRenderHook(() => useNotiLoad({}));

    await waitForNextUpdate();

    expect(result.current.data).toEqual(MOCK_NOTI);
  });
});
