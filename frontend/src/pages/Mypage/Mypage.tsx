import React from 'react';

import Intro from './Intro/Intro';
import Notification from './Notification/Notification';
import History from './History/History';
import AsyncBoundary from 'components/AsyncBoundary';
import ErrorFallback from 'components/ErrorFallback/ErrorFallback';
import BaseLayout from 'components/BaseLayout/BaseLayout';
import { ERROR_MSG } from 'constants/message';
import Styled from './Mypage.styles';

const Mypage = () => {
  return (
    <BaseLayout>
      <Styled.Root>
        <AsyncBoundary rejectedFallback={<ErrorFallback message={ERROR_MSG.LOAD_USER_DATA} />}>
          <Intro />
        </AsyncBoundary>
        <AsyncBoundary rejectedFallback={<ErrorFallback message={ERROR_MSG.LOAD_USER_NOTI} />}>
          <Notification />
        </AsyncBoundary>
        <AsyncBoundary rejectedFallback={<ErrorFallback message={ERROR_MSG.LOAD_USER_HISTORY} />}>
          <History />
        </AsyncBoundary>
      </Styled.Root>
    </BaseLayout>
  );
};

export default Mypage;
