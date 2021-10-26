import { customRenderHook, act } from 'test-util';
import useAccessToken from 'hooks/useAccessToken';

describe('useAccessToken 테스트', () => {
  it('accessToken의 값을 변경할 수 있다.', async () => {
    const { result } = customRenderHook(() => useAccessToken());

    const [_, setAccessToken] = result.current;

    act(() => {
      setAccessToken({
        value: '1a2b3c',
        expiredIn: 1000 * 60,
      });
    });

    expect(result.current[0].value).toBe('1a2b3c');
  });
});
