import { customRenderHook } from 'test-util';
import useTechsLoad from 'hooks/queries/useTechsLoad';
import { MOCK_TECHS } from '__mocks__/fixture/tags';

describe('useTechsLoad 테스트', () => {
  beforeEach(() => {
    localStorage.removeItem('accessToken');
  });

  it('기술 스택 이름을 서버에 보내서 스택 id를 가져올 수 있다.', async () => {
    const { result, waitForNextUpdate } = customRenderHook(() =>
      useTechsLoad({
        techs: 'react',
      }),
    );

    await waitForNextUpdate();

    expect(result.current.data).toEqual(MOCK_TECHS);
  });
});
