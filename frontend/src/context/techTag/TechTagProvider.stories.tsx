import React from 'react';
import TechChip from './chip/TechChips';

import TechInput from './input/TechInput';
import TechTagProvider from './TechTagProvider';

export default {
  title: 'context/TechInput',
  component: TechInput,
  argTypes: {},
};

export const Default = () => (
  <TechTagProvider>
    <TechChip />
    <div>예에에에 성공~~~💯</div>
    <TechInput
      onUpdateTechs={() => {
        console.log('새로운 태그 입력됨');
      }}
    />
  </TechTagProvider>
);
