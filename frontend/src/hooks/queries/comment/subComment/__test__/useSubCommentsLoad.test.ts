import useSubCommentsLoad from 'hooks/queries/comment/subComment/useSubCommentsLoad';
import { customRenderHook } from 'test-util';
import { MOCK_SUB_COMMENTS } from '__mocks__/fixture/comments';

describe('useSubCommentsLoad 테스트', () => {
  beforeEach(() => {
    localStorage.removeItem('accessToken');
  });

  it('댓글의 대댓글을 가져올 수 있다.', async () => {
    const { result, waitForNextUpdate } = customRenderHook(() =>
      useSubCommentsLoad({
        feedId: 2,
        parentCommentId: 1,
      }),
    );

    await waitForNextUpdate();

    expect(result.current.data).toEqual(MOCK_SUB_COMMENTS);
  });
});
