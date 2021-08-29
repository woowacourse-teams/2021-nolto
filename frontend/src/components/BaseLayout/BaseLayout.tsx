import React from 'react';

import Header from 'components/Header/Header';
import Styled, { BaseLayoutMain } from './BaseLayout.styles';

interface Props {
  children: {
    header?: React.ReactNode;
    main: React.ReactNode;
  };
}

const BaseLayout = ({ children: { header = <Header />, main } }: Props) => {
  return (
    <Styled.Root>
      {header}
      <BaseLayoutMain>{main}</BaseLayoutMain>
    </Styled.Root>
  );
};

export default BaseLayout;
