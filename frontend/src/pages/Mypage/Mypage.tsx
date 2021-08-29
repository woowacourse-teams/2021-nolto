import React from 'react';

import Intro from './Intro/Intro';
import Notification from './Notification/Notification';
import History from './History/History';
import AsyncBoundary from 'components/AsyncBoundary';
import ErrorFallback from 'components/ErrorFallback/ErrorFallback';
import BaseLayout from 'components/BaseLayout/BaseLayout';
import Styled from './Mypage.styles';

const Mypage = () => {
  return (
    <BaseLayout>
      {{
        main: (
          <Styled.Root>
            <AsyncBoundary
              rejectedFallback={<ErrorFallback message="사용자 데이터를 가져올 수 없습니다." />}
            >
              <Intro />
            </AsyncBoundary>
            <AsyncBoundary
              rejectedFallback={<ErrorFallback message="알림 목록을 가져올 수 없습니다." />}
            >
              <Notification />
            </AsyncBoundary>
            <AsyncBoundary
              rejectedFallback={<ErrorFallback message="사용자 히스토리를 가져올 수 없습니다." />}
            >
              <History />
            </AsyncBoundary>
          </Styled.Root>
        ),
      }}
    </BaseLayout>
  );
};

export default Mypage;
