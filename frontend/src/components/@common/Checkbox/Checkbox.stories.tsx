import React from 'react';

import Checkbox, { Props } from './Checkbox';

export default {
  title: 'components/common/Checkbox',
  component: Checkbox,
  parameters: { actions: { argTypesRegex: '^on.*' } },
};

const Template = (args: Props) => <Checkbox {...args} />;

export const Default = Template.bind({});

Default.args = {
  labelText: 'Lv.1',
};
