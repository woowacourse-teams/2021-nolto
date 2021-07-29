import React from 'react';
import { renderHook, act } from '@testing-library/react-hooks';

import { wrapper, errorHandler } from '../hookUtils';
import useHotFeeds from 'hooks/queries/useHotFeeds';
import { feeds } from '../fixture/Feeds';

describe('인기 피드 query hook 테스트', async () => {
  const { result, waitFor } = renderHook(
    () =>
      useHotFeeds({
        errorHandler,
      }),
    { wrapper },
  );

  await waitFor(() => result.current.isSuccess);

  expect(result.current.data).toEqual(feeds);
});
