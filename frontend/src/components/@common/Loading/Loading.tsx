import React from 'react';

import loading from 'assets/loading.gif';
import Styled from './Loading.styles';

const Loading = () => {
  return (
    <>
      <Styled.Loading src={loading} />
    </>
  );
};

export default Loading;
