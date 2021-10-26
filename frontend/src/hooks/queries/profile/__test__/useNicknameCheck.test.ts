import useNicknameCheck from 'hooks/queries/profile/useNicknameCheck';
import { customRenderHook } from 'test-util';

describe('useNicknameCheck 테스트', () => {
  it('닉네임 중복체크를 할 수 있다.', async () => {
    localStorage.setItem('accessToken', 'token');

    const { result, waitForNextUpdate } = customRenderHook(() =>
      useNicknameCheck({
        nickname: '닉네임',
      }),
    );

    await waitForNextUpdate();

    expect(result.current.data).toEqual({
      isUsable: true,
    });
  });
});
