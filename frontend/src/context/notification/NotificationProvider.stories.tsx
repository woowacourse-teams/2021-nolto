import React from 'react';

import NotificationProvider from './NotificationProvider';
import useNotification from './useNotification';

export default {
  title: 'context/NotificationProvider',
  component: NotificationProvider,
  argTypes: {},
};

const Page = () => {
  const notification = useNotification();

  return (
    <div>
      <button onClick={() => notification.alert('alert 창인데요?')}>alert</button>
      <button onClick={() => notification.confirm('confirm 창인데요?', () => console.log('성공!'))}>
        confirm
      </button>
    </div>
  );
};

export const Default = () => <Page />;
