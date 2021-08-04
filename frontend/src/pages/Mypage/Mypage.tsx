import React from 'react';

import Intro from './Intro/Intro';
import Notification from './Notification/Notification';
import History from './History/History';
import Header from 'components/Header/Header';
import Styled from './Mypage.styles';

const Mypage = () => {
  return (
    <>
      <Header />
      <Styled.Root>
        <Intro />
        <Notification />
        <History />
      </Styled.Root>
    </>
  );
};

export default Mypage;
