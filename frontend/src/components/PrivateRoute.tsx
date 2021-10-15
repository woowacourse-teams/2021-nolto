import React from 'react';
import { Redirect, Route, RouteProps } from 'react-router-dom';

import useMember from 'contexts/member/useMember';
import useModal from 'contexts/modal/useModal';
import useDialog from 'contexts/dialog/useDialog';
import ROUTE from 'constants/routes';
import LoginModal from './LoginModal/LoginModal';

interface Props extends RouteProps {
  children: React.ReactNode;
}

const PrivateRoute = ({ children, ...props }: Props) => {
  const { userInfo } = useMember();
  const modal = useModal();
  const dialog = useDialog();

  const openLoginModal = () => {
    dialog.confirm('로그인이 필요한 서비스입니다.', () => modal.openModal(<LoginModal />));
  };

  return (
    <Route
      {...props}
      render={({ location }) => {
        if (userInfo) {
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
