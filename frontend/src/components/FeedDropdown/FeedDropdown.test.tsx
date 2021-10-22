import React from 'react';
import { Router } from 'react-router-dom';
import { createMemoryHistory } from 'history';

import { customRender, fireEvent, waitFor } from 'test-util';
import { MOCK_FEED_DETAIL } from '__mocks__/fixture/feeds';
import FeedDropdown from './FeedDropdown';

describe('FeedDropdown 테스트', () => {
  beforeAll(() => {
    localStorage.setItem('accessToken', 'mock123abc');
  });

  it('수정 버튼 클릭 시 수정 페이지로 이동한다.', async () => {
    const history = createMemoryHistory();

    const { getByText } = customRender(
      <Router history={history}>
        <FeedDropdown feedDetail={MOCK_FEED_DETAIL} />
      </Router>,
    );

    const editButton = getByText('수정');
    fireEvent.click(editButton);

    await waitFor(() => {
      expect(history.location.pathname).toBe('/modify');
    });
  });

  it('삭제 버튼 클릭 시 삭제 확인창이 뜬다.', async () => {
    const { getByText } = customRender(<FeedDropdown feedDetail={MOCK_FEED_DETAIL} />);

    const deleteButton = getByText('삭제');
    fireEvent.click(deleteButton);

    expect(getByText('정말로 삭제하시겠습니까?')).toBeInTheDocument();
  });
});
