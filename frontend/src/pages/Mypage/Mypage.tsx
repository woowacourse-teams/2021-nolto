import React from 'react';
import { Helmet } from 'react-helmet-async';

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
      <Helmet>
        <title>놀토: 마이페이지</title>
      </Helmet>
      <Styled.Root>
        <AsyncBoundary rejectedFallback={<ErrorFallback message={ERROR_MSG.LOAD_USER_DATA} />}>
          <Intro />
          <Styled.FlexContainer>
            <Notification />
            <History />
          </Styled.FlexContainer>
        </AsyncBoundary>
      </Styled.Root>
    </BaseLayout>
  );
};

export default Mypage;
