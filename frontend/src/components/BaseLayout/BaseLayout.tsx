import React from 'react';

import Header from 'components/Header/Header';
import Styled from './BaseLayout.styles';

interface Props {
  children: React.ReactNode;
}

const BaseLayout = ({ children }: Props) => {
  return (
    <Styled.Root>
      <Header />
      <Styled.BaseLayoutMain>{children}</Styled.BaseLayoutMain>
    </Styled.Root>
  );
};

export default BaseLayout;
