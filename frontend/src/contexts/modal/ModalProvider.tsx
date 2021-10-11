import React, { ReactNode, useMemo, useState } from 'react';
import ReactDOM from 'react-dom';

import CrossMark from 'assets/crossMark.svg';
import { PALETTE } from 'constants/palette';
import hasWindow from 'constants/windowDetector';
import Styled from './ModalProvider.styles';

interface Props {
  children: ReactNode;
}

interface ModalContext {
  openModal: (modalComponent: ReactNode) => void;
  closeModal: () => void;
}

export const Context = React.createContext<ModalContext | null>(null);

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
          <CrossMark width="1rem" fill={PALETTE.WHITE_400} />
        </Styled.CrossMarkButton>
        {modal && modal}
      </Styled.ModalInner>
    </Styled.ModalContainer>
  );

  const contextValue = useMemo(() => ({ openModal, closeModal }), []);

  return (
    <Context.Provider value={contextValue}>
      {children}
      {hasWindow &&
        isOpen &&
        ReactDOM.createPortal(modalElement, document.getElementById('modal-root'))}
    </Context.Provider>
  );
};

export default ModalProvider;
