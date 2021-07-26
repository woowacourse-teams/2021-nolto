import React from 'react';

import FeedUploadForm from './FeedUploadForm';

export default {
  title: 'components/FeedUploadForm',
  component: FeedUploadForm,
  argTypes: {},
};

export const Default = () => (
  <FeedUploadForm
    onFeedSubmit={() => {
      console.log('제출되었습니다');
    }}
  />
);
