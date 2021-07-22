import React, { createContext, useMemo } from 'react';
import ReactDOM from 'react-dom';

import Styled from './SnackBarProvider.styles';
import ErrorSignIcon from 'assets/errorSign.svg';
import SuccessSignIcon from 'assets/successSign.svg';
import useSnackBarProvider from './useSnackBarProvider';
import { AddSnackBar } from 'types';

interface Props {
  children: React.ReactNode;
}

interface SnackBarContext {
  addSnackBar: AddSnackBar;
}

export const Context = createContext<SnackBarContext>(null);

const snackBarRoot = document.getElementById('snackbar-root');

const SnackBarProvider = ({ children }: Props) => {
  const { snackBars, addSnackBar } = useSnackBarProvider({
    maxSnackBarCount: 3,
    snackBarRemainTime: 2000,
  });

  const snackBarSignMap = {
    error: <ErrorSignIcon width="1.5rem" />,
    success: <SuccessSignIcon width="1.5rem" />,
  };

  const contextValue = useMemo(() => ({ addSnackBar }), []);

  const snackBarElement = (
    <Styled.SnackBarWrapper>
      {snackBars.map((snackBar) => (
        <Styled.SnackBar key={snackBar.key} type={snackBar.type}>
          {snackBarSignMap[snackBar.type]}
          <span>{snackBar.text}</span>
        </Styled.SnackBar>
      ))}
    </Styled.SnackBarWrapper>
  );

  return (
    <Context.Provider value={contextValue}>
      {children}
      {ReactDOM.createPortal(snackBarElement, snackBarRoot)}
    </Context.Provider>
  );
};

export default SnackBarProvider;
