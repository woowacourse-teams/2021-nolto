import useFeedDetail from 'hooks/queries/feed/useFeedDetail';
import { customRenderHook } from 'test-util';
import { MOCK_FEED_DETAIL } from '__mocks__/fixture/feeds';

describe('useFeedDetail 테스트', () => {
  beforeEach(() => {
    localStorage.removeItem('accessToken');
  });

  it('피드 상세 내용을 가져올 수 있다.', async () => {
    const { result, waitForNextUpdate } = customRenderHook(() =>
      useFeedDetail({
        feedId: 2,
      }),
    );

    await waitForNextUpdate();

    expect(result.current.data).toEqual(MOCK_FEED_DETAIL);
  });
});
