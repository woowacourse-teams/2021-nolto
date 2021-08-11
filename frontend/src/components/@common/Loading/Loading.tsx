import React from 'react';

import loading from 'assets/loading.gif';
import Styled from './Loading.styles';

const Loading = () => {
  return (
    <Styled.Root>
      <img src={loading} />
    </Styled.Root>
  );
};

export default Loading;
