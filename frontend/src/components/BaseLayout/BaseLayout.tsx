import React from 'react';

import Header from 'components/Header/Header';
import Styled, { BaseLayoutMain } from './BaseLayout.styles';

interface Props {
  header?: React.ReactNode;
  children: React.ReactNode;
}

const BaseLayout = ({ header = <Header />, children }: Props) => {
  return (
    <Styled.Root>
      {header}
      <BaseLayoutMain>{children}</BaseLayoutMain>
    </Styled.Root>
  );
};

export default BaseLayout;
