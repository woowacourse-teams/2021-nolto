import React, { ReactNode, useState } from 'react';
import ReactDOM from 'react-dom';

import CrossMark from 'assets/crossMark.svg';
import Styled from './ModalProvider.styles';

interface Props {
  children: ReactNode;
}

interface ModalContext {
  openModal: (modalComponent: ReactNode) => void;
  closeModal: () => void;
}

export const Modal = React.createContext<ModalContext | null>(null);
const modalRoot = document.getElementById('modal-root');

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

  const modalElement: React.ReactNode = (
    <Styled.ModalContainer>
      <Styled.ModalInner>
        <Styled.CrossMarkButton onClick={closeModal}>
          <CrossMark width="16px" />
        </Styled.CrossMarkButton>
        {modal && modal}
      </Styled.ModalInner>
    </Styled.ModalContainer>
  );

  return (
    <Modal.Provider value={{ openModal, closeModal }}>
      {children}
      {isOpen && ReactDOM.createPortal(modalElement, modalRoot)}
    </Modal.Provider>
  );
};

export default ModalProvider;
