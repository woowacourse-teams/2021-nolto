import React, { useMemo, useState } from 'react';
import ReactDOM from 'react-dom';

import Styled, { Button } from './DialogProvider.styles';
import { ButtonStyle, DialogType } from 'types';

interface Props {
  children: React.ReactNode;
}

interface DialogContext {
  alert: (message: string) => void;
  confirm: (message: string, callback: OnConfirm) => void;
}

type OnConfirm = () => unknown;

export const Context = React.createContext<DialogContext>(null);

const dialogRoot = document.getElementById('dialog-root');

const DialogProvider = ({ children }: Props) => {
  const [dialogType, setDialogType] = useState<DialogType>();
  const [message, setMessage] = useState('');
  const [isOpen, setIsOpen] = useState(false);
  const [onConfirm, setOnConfirm] = useState<OnConfirm | null>(null);

  const alert = (msg: string) => {
    setDialogType('alert');
    setMessage(msg);
    setIsOpen(true);
  };

  const confirm = (msg: string, callback: OnConfirm) => {
    setDialogType('confirm');
    setMessage(msg);
    setIsOpen(true);
    setOnConfirm(() => callback);
  };

  const confirmDialog = () => {
    if (onConfirm) {
      onConfirm();
    }

    setIsOpen(false);
  };

  const cancelDialog = () => {
    setIsOpen(false);
  };

  const contextValue = useMemo(() => ({ alert, confirm }), []);

  const dialogButtonMap = {
    alert: (
      <Button single buttonStyle={ButtonStyle.SOLID} onClick={confirmDialog}>
        확인
      </Button>
    ),
    confirm: (
      <>
        <Button buttonStyle={ButtonStyle.SOLID} onClick={confirmDialog}>
          확인
        </Button>
        <Button buttonStyle={ButtonStyle.OUTLINE} onClick={cancelDialog}>
          취소
        </Button>
      </>
    ),
  };

  const dialogElement: React.ReactNode = (
    <Styled.DialogContainer>
      <Styled.DialogInner>
        <Styled.TopBar>
          <Styled.AlertTitle>알림</Styled.AlertTitle>
        </Styled.TopBar>
        <div>{message}</div>
        <Styled.ButtonsContainer>{dialogButtonMap[dialogType]}</Styled.ButtonsContainer>
      </Styled.DialogInner>
    </Styled.DialogContainer>
  );

  return (
    <Context.Provider value={contextValue}>
      {children}
      {isOpen && ReactDOM.createPortal(dialogElement, dialogRoot)}
    </Context.Provider>
  );
};

export default DialogProvider;
