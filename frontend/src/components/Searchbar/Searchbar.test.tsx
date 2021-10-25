import React from 'react';
import { Router } from 'react-router-dom';
import { createMemoryHistory } from 'history';

import { customRender, waitFor, fireEvent } from 'test-util';
import Searchbar from './Searchbar';

describe('Searchbar 테스트', () => {
  it('제목/내용으로 검색이 가능하다.', async () => {
    const history = createMemoryHistory();

    const { getByRole } = customRender(
      <Router history={history}>
        <Searchbar />
      </Router>,
    );

    const searchInput = getByRole('textbox', {
      name: /제목\/내용으로 검색/i,
    });
    const searchButton = getByRole('button', { name: /검색/i });

    fireEvent.change(searchInput, { target: { value: '테스트검색' } });
    fireEvent.click(searchButton);

    await waitFor(() => {
      expect(history.location.pathname).toBe('/search');
    });
  });
});
