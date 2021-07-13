import React, { ReactNode, useContext, useState } from 'react';

import CrossMark from 'assets/crossMark.svg';
import Styled from './ModalProvider.styles';

interface Props {
  children: ReactNode;
}

interface ModalContext {
  openModal: (modalComponent: ReactNode) => void;
  closeModal: () => void;
}

const Modal = React.createContext<ModalContext | null>(null);

const ModalProvider = ({ children }: Props) => {
  const [modal, setModal] = useState<ReactNode | null>(null);
  const [isOpen, setIsOpen] = useState(false);

  const openModal = (modalComponent: ReactNode) => {
    setModal(modalComponent);
    setIsOpen(true);
  };

  const closeModal = () => {
    setIsOpen(false);
  };

  return (
    <Modal.Provider value={{ openModal, closeModal }}>
      {children}
      {isOpen && (
        <Styled.ModalContainer>
          <Styled.ModalInner>
            <Styled.CrossMarkButton onClick={() => closeModal()}>
              <CrossMark />
            </Styled.CrossMarkButton>
            {modal && modal}
          </Styled.ModalInner>
        </Styled.ModalContainer>
      )}
    </Modal.Provider>
  );
};

export default ModalProvider;

export const useModal = () => {
  const context = useContext(Modal);

  if (!context) {
    throw new Error('ModalProvider 내부에서만 useModal hook을 사용할 수 있습니다.');
  }

  return context;
};
