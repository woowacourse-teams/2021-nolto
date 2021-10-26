import useHotFeedsLoad from 'hooks/queries/feed/useHotFeedsLoad';
import { customRenderHook } from 'test-util';
import { MOCK_HOT_FEEDS } from '__mocks__/fixture/feeds';

describe('useHotFeedsLoad 테스트', () => {
  beforeEach(() => {
    localStorage.removeItem('accessToken');
  });

  it('hot feed 목록을 가져올 수 있다.', async () => {
    const { result, waitForNextUpdate } = customRenderHook(() => useHotFeedsLoad({}));

    await waitForNextUpdate();

    expect(result.current.data).toEqual(MOCK_HOT_FEEDS);
  });
});
