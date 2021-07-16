import React from 'react';

import RadioButton, { Props } from './RadioButton';

export default {
  title: 'components/common/RadioButton',
  component: RadioButton,
  parameters: { actions: { argTypesRegex: '^on.*' } },
};

const Template = (args: Props) => <RadioButton {...args} />;

export const Default = Template.bind({});

Default.args = {
  labelText: 'Lv.1',
};
