import React, { ReactNode, useMemo, useState } from 'react';
import ReactDOM from 'react-dom';

import CrossMark from 'assets/crossMark.svg';
import Styled from './ModalProvider.styles';
import { PALETTE } from 'constants/palette';

interface Props {
  children: ReactNode;
}

interface ModalContext {
  openModal: (modalComponent: ReactNode) => void;
  closeModal: () => void;
}

export const Context = React.createContext<ModalContext | null>(null);
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

  const handleClickDimmed = (event: React.MouseEvent<HTMLDivElement>) => {
    if (event.target === event.currentTarget) {
      closeModal();
    }
  };

  const modalElement: React.ReactNode = (
    <Styled.ModalContainer onMouseDown={handleClickDimmed}>
      <Styled.ModalInner>
        <Styled.CrossMarkButton onClick={closeModal}>
          <CrossMark width="18px" fill={PALETTE.WHITE_400} />
        </Styled.CrossMarkButton>
        {modal && modal}
      </Styled.ModalInner>
    </Styled.ModalContainer>
  );

  const contextValue = useMemo(() => ({ openModal, closeModal }), []);

  return (
    <Context.Provider value={contextValue}>
      {children}
      {isOpen && ReactDOM.createPortal(modalElement, modalRoot)}
    </Context.Provider>
  );
};

export default ModalProvider;
