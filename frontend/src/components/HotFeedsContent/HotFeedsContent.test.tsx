import React from 'react';

import { customRender, waitFor } from 'test-util';
import { MOCK_HOT_FEEDS } from '__mocks__/fixture/feeds';
import HotFeedsContent from './HotFeedsContent';

describe('HotFeedsContent 테스트', () => {
  it('인기 프로젝트 피드 목록을 보여준다.', async () => {
    const { getByText } = customRender(<HotFeedsContent />);

    await waitFor(() => {
      MOCK_HOT_FEEDS.forEach((hotFeed) => {
        expect(getByText(hotFeed.title)).toBeInTheDocument();
      });
    });
  });
});
