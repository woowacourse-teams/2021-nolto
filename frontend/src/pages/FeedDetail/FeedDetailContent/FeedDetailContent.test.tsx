import React from 'react';

import { customRender } from 'test-util';
import FeedDetailContent from 'pages/FeedDetail/FeedDetailContent/FeedDetailContent';

describe('FeedDetailContent 테스트', () => {
  it.only('피드 상세 페이지를 불러온다.', async () => {
    customRender(<FeedDetailContent id={1} />);
  });
});
