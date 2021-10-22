import useCommentsLoad from 'hooks/queries/comment/useCommentsLoad';
import { customRenderHook } from 'test-util';
import { MOCK_COMMENTS } from '__mocks__/fixture/comments';

describe('useCommentsLoad 테스트', () => {
  beforeEach(() => {
    localStorage.removeItem('accessToken');
  });

  it('게시물 댓글을 불러올 수 있다.', async () => {
    const { result, waitForNextUpdate } = customRenderHook(() =>
      useCommentsLoad({
        feedId: 1,
      }),
    );

    await waitForNextUpdate();

    expect(result.current.data).toEqual(MOCK_COMMENTS);
  });
});
