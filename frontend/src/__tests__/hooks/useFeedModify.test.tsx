import { customRenderHook } from 'test-util';
import useFeedModify from 'hooks/mutations/useFeedModify';

describe('useFeedModify 테스트', () => {
  it('피드를 수정할 수 있다.', async () => {
    const { result, waitFor } = customRenderHook(() => useFeedModify());

    result.current.mutate({ feedId: 1, formData: new FormData() });

    await waitFor(() => result.current.isSuccess);
  });
});
