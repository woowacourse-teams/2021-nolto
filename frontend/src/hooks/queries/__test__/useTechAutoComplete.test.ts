import { customRenderHook } from 'test-util';
import useTechAutoComplete from 'hooks/queries/useTechAutoComplete';
import { MOCK_TECHS } from '__mocks__/fixture/tags';

describe('useTechAutoComplete 테스트', () => {
  beforeEach(() => {
    localStorage.removeItem('accessToken');
  });

  it('유저 정보를 가져올 수 있다.', async () => {
    const { result, waitForNextUpdate } = customRenderHook(() =>
      useTechAutoComplete({
        autoComplete: 'mockAutoComplete',
      }),
    );

    await waitForNextUpdate();

    expect(result.current.data).not.toBe(MOCK_TECHS);
  });
});
