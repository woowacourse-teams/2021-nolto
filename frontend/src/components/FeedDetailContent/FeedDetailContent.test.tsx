import React from 'react';
import ReactDOM from 'react-dom';

import { fireEvent, waitFor, screen } from '@testing-library/react';
import { customRender } from 'test-util';

import FeedDetailContent from 'components/FeedDetailContent/FeedDetailContent';
import useFeedDetail from 'hooks/queries/useFeedDetail';
import { mockFeeds, mockFeedDetail } from '__mocks__/fixture/Feeds';

jest.mock('hooks/queries/useFeedDetail', () => {
  return jest.fn(() => ({
    data: mockFeedDetail,
  }));
});

describe('FeedDetailContent 테스트', () => {
  beforeAll(() => {
    ReactDOM.createPortal = jest.fn((element, node) => {
      return element as React.ReactPortal;
    });
  });

  afterEach(() => {
    // ReactDOM.createPortal.mockClear();
  });

  it.only('피드 상세 페이지를 불러온다.', async () => {
    customRender(<FeedDetailContent id={1} />);
  });

  it('피드에 좋아요를 누를 수 있다.', async () => {
    customRender(<FeedDetailContent id={1} />);
  });

  it('피드에 좋아요를 취소할 수 있다.', async () => {
    customRender(<FeedDetailContent id={1} />);
  });

  it('기술 스택을 클릭할 수 있다.', async () => {
    customRender(<FeedDetailContent id={1} />);
  });

  it('기술 스택을 클릭할 수 있다.', async () => {
    customRender(<FeedDetailContent id={1} />);
  });
});
