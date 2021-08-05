import { customRenderHook } from 'test-util';
import useFeedUnlike from 'hooks/mutations/useFeedUnlike';

describe('useFeedUnlike 테스트', () => {
  it('피드 좋아요를 취소할 수 있다.', async () => {
    const { result, waitFor } = customRenderHook(() => useFeedUnlike());

    result.current.mutate({ feedId: 1 });

    await waitFor(() => result.current.isSuccess);
  });
});
