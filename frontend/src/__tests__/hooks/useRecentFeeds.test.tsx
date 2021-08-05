import { customRenderHook } from 'test-util';
import useRecentFeeds from 'hooks/queries/useRecentFeeds';
import { FilterType } from 'types';
import { MOCK_FEEDS } from '__mocks__/fixture/Feeds';

describe('useRecentFeeds 테스트', () => {
  it('최신 피드를 불러올 수 있다.', async () => {
    const { result, waitFor } = customRenderHook(() =>
      useRecentFeeds({ filter: FilterType.PROGRESS }),
    );

    await waitFor(() => result.current.isSuccess);

    expect(result.current.data).toEqual(MOCK_FEEDS);
  });
});
