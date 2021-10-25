import React from 'react';

import { customRender, waitFor } from 'test-util';
import { MOCK_RECENT_FEEDS } from '__mocks__/fixture/feeds';
import RecentFeedsContent from './RecentFeedsContent';

/* eslint-disable @typescript-eslint/no-explicit-any */
beforeEach(() => {
  const mockIntersectionObserver = jest.fn();
  mockIntersectionObserver.mockReturnValue({
    observe: () => null as any,
    unobserve: () => null as any,
    disconnect: () => null as any,
  });
  window.IntersectionObserver = mockIntersectionObserver;
});

describe('RecentFeedsContent 테스트', () => {
  it('최신 프로젝트 피드 목록을 보여준다.', async () => {
    const { getByText } = customRender(<RecentFeedsContent />);

    await waitFor(() => {
      MOCK_RECENT_FEEDS.feeds.forEach((recentFeed) => {
        expect(getByText(recentFeed.title)).toBeInTheDocument();
      });
    });
  });
});
