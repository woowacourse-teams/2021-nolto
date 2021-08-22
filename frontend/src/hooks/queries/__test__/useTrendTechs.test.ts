import { customRenderHook } from 'test-util';
import useTrendTechs from 'hooks/queries/useTrendTechs';
import { MOCK_TECHS } from '__mocks__/fixture/tags';

describe('useTrendTechs 테스트', () => {
  beforeEach(() => {
    localStorage.removeItem('accessToken');
  });

  it('최신 트렌드 기술 스택 가져올 수 있다.', async () => {
    const { result, waitForNextUpdate } = customRenderHook(() => useTrendTechs({}));

    await waitForNextUpdate();

    expect(result.current.data).toEqual(MOCK_TECHS);
  });
});
