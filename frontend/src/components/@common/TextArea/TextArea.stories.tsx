import React from 'react';

import TextArea from './TextArea';

export default {
  title: 'components/common/TextArea',
  component: TextArea,
  argTypes: {},
};

export const Default = () => (
  <div style={{ height: '100vh' }}>
    <TextArea placeholder="요건 넓은 텍스트 인풋" />
  </div>
);
