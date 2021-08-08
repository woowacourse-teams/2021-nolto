import React from 'react';

import { customRender, fireEvent, waitFor } from 'test-util';
import { MOCK_FEED_DETAIL } from '__mocks__/fixture/Feeds';
import { MOCK_USER } from '__mocks__/fixture/User';
import LikeButton from './LikeButton';

jest.mock('hooks/queries/useMember', () => {
  return () => ({
    userData: MOCK_USER.ZIG,
    isLogin: true,
    logout: () => console.log('logout'),
  });
});

describe('LikeButton 테스트', () => {
  it('좋아요하지 않은 피드에서 좋아요 버튼을 누르면 좋아요 수가 1 증가한다.', async () => {
    const { getByRole, container } = customRender(<LikeButton feedDetail={MOCK_FEED_DETAIL} />);

    const likeButton = getByRole('button');
    const likeCountBefore = container.querySelector('span');

    expect(likeCountBefore).toHaveTextContent(MOCK_FEED_DETAIL.likes.toString());

    fireEvent.click(likeButton);

    const likeCountAfter = container.querySelector('span');

    await waitFor(() => {
      expect(likeCountAfter).toHaveTextContent((MOCK_FEED_DETAIL.likes + 1).toString());
    });
  });

  it('좋아요한 피드에서 좋아요 버튼을 누르면 좋아요 수가 1 감소한다.', async () => {
    const { getByRole, container } = customRender(
      <LikeButton feedDetail={{ ...MOCK_FEED_DETAIL, liked: true }} />,
    );

    const likeButton = getByRole('button');
    const likeCountBefore = container.querySelector('span');

    expect(likeCountBefore).toHaveTextContent(MOCK_FEED_DETAIL.likes.toString());

    fireEvent.click(likeButton);

    const likeCountAfter = container.querySelector('span');

    await waitFor(() => {
      expect(likeCountAfter).toHaveTextContent((MOCK_FEED_DETAIL.likes - 1).toString());
    });
  });
});
