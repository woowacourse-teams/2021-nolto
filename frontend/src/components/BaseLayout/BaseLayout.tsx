import React from 'react';

import Header from 'components/Header/Header';
import Styled from './BaseLayout.styles';

interface Props {
  children: React.ReactNode;
}

const BaseLayout = ({ children }: Props) => {
  return (
    <section>
      <Header />
      {children}
    </section>
  );
};

export default BaseLayout;
