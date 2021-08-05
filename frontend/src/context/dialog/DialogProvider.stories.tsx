import React from 'react';

import DialogProvider from './DialogProvider';
import useDialog from './useDialog';

export default {
  title: 'context/DialogProvider',
  component: DialogProvider,
  argTypes: {},
};

const Page = () => {
  const dialog = useDialog();

  return (
    <div>
      <button onClick={() => dialog.alert('alert 창인데요?')}>alert</button>
      <button onClick={() => dialog.confirm('confirm 창인데요?', () => console.log('성공!'))}>
        confirm
      </button>
    </div>
  );
};

export const Default = () => (
  <DialogProvider>
    <Page />
  </DialogProvider>
);
