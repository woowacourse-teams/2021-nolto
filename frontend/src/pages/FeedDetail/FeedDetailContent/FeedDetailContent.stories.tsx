import React from 'react';

import FeedDetailContent from './FeedDetailContent';

export default {
  title: 'components/FeedDetailContent',
  component: FeedDetailContent,
  argTypes: {},
};

const Template = (args: { feedId: number }) => <FeedDetailContent {...args} />;

export const Default = Template.bind({});

Default.args = {
  feedId: 4,
};
