import React from 'react';

import Toggle, { Props } from './Toggle';

export default {
  title: 'components/common/Toggle',
  component: Toggle,
  parameters: { actions: { argTypesRegex: '^on.*' } },
};

const Template = (args: Props) => <Toggle {...args} />;

export const Default = Template.bind({});

Default.args = {
  labelText: 'SOS',
};
