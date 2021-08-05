import { customRenderHook } from 'test-util';
import useFeedLike from 'hooks/mutations/useFeedLike';

describe('useFeedLike 테스트', () => {
  it('피드에 좋아요를 누를 수 있다.', async () => {
    const { result, waitFor } = customRenderHook(() => useFeedLike());

    result.current.mutate({ feedId: 1 });

    await waitFor(() => result.current.isSuccess);
  });
});
