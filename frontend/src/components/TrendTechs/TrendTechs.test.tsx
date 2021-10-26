import React from 'react';

import { customRender, waitFor } from 'test-util';
import { MOCK_TECHS } from '__mocks__/fixture/tags';
import TrendTechs from './TrendTechs';

describe('TrendTechs 테스트', () => {
  it('인기 검색어 목록을 보여준다.', async () => {
    const { getByText } = customRender(<TrendTechs />);

    await waitFor(() => {
      MOCK_TECHS.forEach((tech) => {
        expect(getByText(tech.text)).toBeInTheDocument();
      });
    });
  });
});
