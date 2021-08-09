import React, { useEffect } from 'react';

import LoginModal from './LoginModal';
import useModal from 'contexts/modal/useModal';

export default {
  title: 'components/LoginModal',
  component: LoginModal,
  argTypes: {},
};

const Page = () => {
  const modal = useModal();
  const TestModal = <LoginModal />;

  useEffect(() => {
    modal.openModal(TestModal);
  }, []);

  return (
    <div>
      <button onClick={() => modal.openModal(TestModal)}>모달 열기</button>
    </div>
  );
};

export const Default = () => {
  return <Page />;
};
