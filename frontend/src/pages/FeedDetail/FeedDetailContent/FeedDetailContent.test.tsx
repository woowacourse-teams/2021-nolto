import React from 'react';

import { customRender, waitFor } from 'test-util';
import FeedDetailContent from './FeedDetailContent';
import { MOCK_FEED_DETAIL } from '__mocks__/fixture/Feeds';

describe('FeedDetailContent 테스트', () => {
  beforeAll(() => {
    localStorage.setItem('accessToken', 'mock123abc');
  });

  it('피드 상세 페이지의 세부 항목들을 보여준다.', async () => {
    const { getByText, getByRole } = customRender(<FeedDetailContent id={1} />);

    await waitFor(() => {
      expect(
        getByRole('heading', {
          name: MOCK_FEED_DETAIL.title,
        }),
      ).toBeInTheDocument();

      expect(getByText(MOCK_FEED_DETAIL.author.nickname)).toBeInTheDocument();
    });
  });
});
