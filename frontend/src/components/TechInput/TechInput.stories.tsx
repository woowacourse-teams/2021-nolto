import React from 'react';

import TechInput from './TechInput';

export default {
  title: 'components/TechInput',
  component: TechInput,
  argTypes: {},
};

export const Default = () => (
  <TechInput
    onUpdateTechs={() => {
      console.log('새로운 태그 입력됨');
    }}
  />
);
