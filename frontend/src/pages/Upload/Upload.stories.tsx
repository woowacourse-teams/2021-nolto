import React from 'react';

import BaseLayout from 'components/BaseLayout/BaseLayout';
import Upload from './Upload';

export default {
  title: 'pages/Upload',
  component: Upload,
  argTypes: {},
};

export const Default = () => (
  <BaseLayout>
    <Upload />
  </BaseLayout>
);
