import React, { useMemo, useState } from 'react';
import ReactDOM from 'react-dom';

import Styled, { Button } from './NotificationProvider.styles';
import { ButtonStyle, NotificationType } from 'types';

interface Props {
  children: React.ReactNode;
}

interface NotificationContext {
  alert: (message: string) => void;
  confirm: (message: string, callback: OnConfirm) => void;
}

type OnConfirm = () => unknown;

export const Context = React.createContext<NotificationContext>(null);

const notificationRoot = document.getElementById('notification-root');

const NotificationProvider = ({ children }: Props) => {
  const [notiType, setNotiType] = useState<NotificationType>();
  const [message, setMessage] = useState('');
  const [isOpen, setIsOpen] = useState(false);
  const [onConfirm, setOnConfirm] = useState<OnConfirm | null>(null);

  const alert = (msg: string) => {
    setNotiType('alert');
    setMessage(msg);
    setIsOpen(true);
  };

  const confirm = (msg: string, callback: OnConfirm) => {
    setNotiType('confirm');
    setMessage(msg);
    setIsOpen(true);
    setOnConfirm(() => callback);
  };

  const confirmNotification = () => {
    if (onConfirm) {
      onConfirm();
    }

    setIsOpen(false);
  };

  const cancelNotification = () => {
    setIsOpen(false);
  };

  const contextValue = useMemo(() => ({ alert, confirm }), []);

  const notiButtonMap = {
    alert: (
      <Button single buttonStyle={ButtonStyle.SOLID} onClick={confirmNotification}>
        확인
      </Button>
    ),
    confirm: (
      <>
        <Button buttonStyle={ButtonStyle.SOLID} onClick={confirmNotification}>
          확인
        </Button>
        <Button buttonStyle={ButtonStyle.OUTLINE} onClick={cancelNotification}>
          취소
        </Button>
      </>
    ),
  };

  const notificationElement: React.ReactNode = (
    <Styled.NotiContainer>
      <Styled.NotiInner>
        <Styled.TopBar>
          <Styled.AlertTitle>알림</Styled.AlertTitle>
        </Styled.TopBar>
        <div>{message}</div>
        <Styled.ButtonsContainer>{notiButtonMap[notiType]}</Styled.ButtonsContainer>
      </Styled.NotiInner>
    </Styled.NotiContainer>
  );

  return (
    <Context.Provider value={contextValue}>
      {children}
      {isOpen && ReactDOM.createPortal(notificationElement, notificationRoot)}
    </Context.Provider>
  );
};

export default NotificationProvider;
