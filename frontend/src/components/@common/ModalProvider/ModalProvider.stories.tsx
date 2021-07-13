import React from 'react';

import ModalProvider, { useModal } from './ModalProvider';

export default {
  title: 'components/common/ModalProvider',
  component: ModalProvider,
  argTypes: {},
};

const Page = () => {
  const modal = useModal();

  return (
    <div>
      <button onClick={() => modal.openModal(<div>모달입니당</div>)}>모달 열기</button>
    </div>
  );
};

export const Default = () => (
  <ModalProvider>
    <Page />
  </ModalProvider>
);
