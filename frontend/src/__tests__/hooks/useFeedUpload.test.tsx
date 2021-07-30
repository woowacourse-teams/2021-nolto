import { customRenderHook } from 'test-util';
import useFeedUpload from 'hooks/mutations/useFeedUpload';

describe('useFeedUpload 테스트', () => {
  it('새로운 피드를 등록할 수 있다.', async () => {
    const { result, waitFor } = customRenderHook(() => useFeedUpload());

    result.current.mutate(new FormData());

    await waitFor(() => result.current.isSuccess);
  });
});
