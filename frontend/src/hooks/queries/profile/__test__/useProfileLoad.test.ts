import useProfileLoad from 'hooks/queries/profile/useProfileLoad';
import { customRenderHook } from 'test-util';
import { MOCK_PROFILE } from '__mocks__/fixture/profile';

describe('useProfileLoad 테스트', () => {
  it('유저 프로필을 가져올 수 있다.', async () => {
    localStorage.setItem('accessToken', 'token');

    const { result, waitForNextUpdate } = customRenderHook(() => useProfileLoad({}));

    await waitForNextUpdate();

    expect(result.current.data).toEqual(MOCK_PROFILE);
  });
});
