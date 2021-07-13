import React, { useEffect } from 'react';

import ModalProvider, { useModal } from './ModalProvider';

export default {
  title: 'components/common/ModalProvider',
  component: ModalProvider,
  argTypes: {},
};

const Page = () => {
  const modal = useModal();
  const TestModal = <div>모달입니당</div>;

  useEffect(() => {
    modal.openModal(TestModal);
  }, []);

  return (
    <div>
      <button onClick={() => modal.openModal(TestModal)}>모달 열기</button>
    </div>
  );
};

export const Default = () => (
  <ModalProvider>
    <Page />
  </ModalProvider>
);
