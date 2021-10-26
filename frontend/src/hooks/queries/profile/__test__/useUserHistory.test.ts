import { customRenderHook } from 'test-util';
import { MOCK_HISTORY } from '__mocks__/fixture/history';

import useUserHistory from '../useUserHistory';

describe('useUserHistory 테스트', () => {
  it('유저 히스토리를 가져올 수 있다.', async () => {
    localStorage.setItem('accessToken', 'token');

    const { result, waitForNextUpdate } = customRenderHook(() => useUserHistory({}));

    await waitForNextUpdate();

    expect(result.current.data).toEqual(MOCK_HISTORY);
  });
});
