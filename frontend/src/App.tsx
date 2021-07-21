import React from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from 'react-query';
import { ReactQueryDevtools } from 'react-query/devtools';

import Home from 'pages/Home/Home';
import Upload from 'pages/Upload/Upload';
import FeedDetail from 'pages/FeedDetail/FeedDetail';
import RecentFeeds from 'pages/RecentFeeds/RecentFeeds';
import ModalProvider from 'components/@common/ModalProvider/ModalProvider';
import UserInfoProvider from 'storage/user/UserInfoProvider';
import ROUTE from 'constants/routes';
import GlobalStyle from './Global.styles';

const queryClient = new QueryClient();

const App = () => {
  return (
    <QueryClientProvider client={queryClient}>
      <GlobalStyle />
      <Router>
        <Switch>
          <UserInfoProvider>
            <ModalProvider>
              <main>
                <Route exact path={ROUTE.HOME}>
                  <Home />
                </Route>
                <Route path={ROUTE.UPLOAD}>
                  <Upload />
                </Route>
                <Route exact path={ROUTE.FEEDS}>
                  <RecentFeeds />
                </Route>
                <Route path={`${ROUTE.FEEDS}/:id`}>
                  <FeedDetail />
                </Route>
              </main>
            </ModalProvider>
          </UserInfoProvider>
        </Switch>
      </Router>
      <ReactQueryDevtools panelProps={{ className: 'query-dev-tools' }} initialIsOpen={false} />
    </QueryClientProvider>
  );
};

export default App;
