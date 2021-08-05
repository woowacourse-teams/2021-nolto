import { customRenderHook } from 'test-util';
import useFeedDelete from 'hooks/mutations/useFeedDelete';

describe('useFeedDelete 테스트', () => {
  it('피드를 삭제할 수 있다.', async () => {
    const { result, waitFor } = customRenderHook(() => useFeedDelete());

    result.current.mutate({ feedId: 1 });

    await waitFor(() => result.current.isSuccess);
  });
});
