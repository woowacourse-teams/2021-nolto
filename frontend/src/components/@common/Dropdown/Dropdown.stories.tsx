import React from 'react';

import Dropdown from './Dropdown';

export default {
  title: 'components/common/Dropdown',
  component: Dropdown,
  argTypes: {},
};

export const Default = () => (
  <Dropdown>
    <button>1</button>
    <button>2</button>
    <button>3</button>
    <button>4</button>
  </Dropdown>
);
