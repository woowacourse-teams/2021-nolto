import React from 'react';
import { Redirect, Route, RouteProps } from 'react-router-dom';

import useMember from 'hooks/queries/useMember';
import useModal from 'context/modal/useModal';
import useDialog from 'context/dialog/useDialog';
import ROUTE from 'constants/routes';
import LoginModal from './LoginModal/LoginModal';

interface Props extends RouteProps {
  children: React.ReactNode;
}

const PrivateRoute = ({ children, ...props }: Props) => {
  const { isLogin } = useMember();
  const modal = useModal();
  const dialog = useDialog();

  const openLoginModal = () => {
    dialog.confirm('로그인이 필요한 서비스입니다.', () => modal.openModal(<LoginModal />));
  };

  return (
    <Route
      {...props}
      render={({ location }) => {
        if (isLogin) {
          return children;
        } else {
          openLoginModal();

          return (
            <Redirect
              to={{
                pathname: ROUTE.HOME,
                state: {
                  from: location,
                },
              }}
            />
          );
        }
      }}
    />
  );
};

export default PrivateRoute;
