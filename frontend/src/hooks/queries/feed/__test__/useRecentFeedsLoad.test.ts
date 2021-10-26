import { customRenderHook } from 'test-util';
import useRecentFeedsLoad from 'hooks/queries/feed/useRecentFeedsLoad';
import { MOCK_RECENT_FEEDS } from '__mocks__/fixture/feeds';

describe('useRecentFeeds 테스트', () => {
  it('최신 피드를 불러올 수 있다.', async () => {
    const { result, waitFor } = customRenderHook(() => useRecentFeedsLoad({}));

    await waitFor(() => result.current.isSuccess);

    expect(result.current.data.pages[0]).toEqual(MOCK_RECENT_FEEDS);
  });
});
