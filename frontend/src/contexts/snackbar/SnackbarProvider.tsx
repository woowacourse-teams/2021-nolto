import React, { createContext, useMemo } from 'react';
import ReactDOM from 'react-dom';

import ErrorSignIcon from 'assets/errorSign.svg';
import SuccessSignIcon from 'assets/successSign.svg';
import useSnackbarProvider from './useSnackbarProvider';
import { AddSnackbar } from 'types';
import Styled from './SnackbarProvider.styles';

interface Props {
  children: React.ReactNode;
}

interface SnackbarContext {
  addSnackbar: AddSnackbar;
}

export const Context = createContext<SnackbarContext>(null);

const SnackbarProvider = ({ children }: Props) => {
  const { snackbars, addSnackbar } = useSnackbarProvider({
    maxSnackbarCount: 3,
    snackbarRemainTime: 2000,
  });

  const snackbarSignMap = {
    error: <ErrorSignIcon width="1.5rem" />,
    success: <SuccessSignIcon width="1.5rem" />,
  };

  const contextValue = useMemo(() => ({ addSnackbar }), []);

  const snackbarElement = (
    <Styled.SnackbarWrapper>
      {snackbars.map((snackbar) => (
        <Styled.Snackbar key={snackbar.key} type={snackbar.type}>
          {snackbarSignMap[snackbar.type]}
          <span>{snackbar.text}</span>
        </Styled.Snackbar>
      ))}
    </Styled.SnackbarWrapper>
  );

  return (
    <Context.Provider value={contextValue}>
      {children}
      {window && ReactDOM.createPortal(snackbarElement, document.getElementById('snackbar-root'))}
    </Context.Provider>
  );
};

export default SnackbarProvider;
