import React from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from 'react-query';
import { ReactQueryDevtools } from 'react-query/devtools';

import Home from 'pages/Home/Home';
import Upload from 'pages/Upload/Upload';
import FeedDetail from 'pages/FeedDetail/FeedDetail';
import RecentFeeds from 'pages/RecentFeeds/RecentFeeds';
import OAuth from 'pages/OAuth/OAuth';
import ModalProvider from 'components/@common/ModalProvider/ModalProvider';
import AsyncBoundary from 'components/AsyncBoundary';
import UserInfoProvider from 'storage/user/UserInfoProvider';
import ROUTE from 'constants/routes';
import GlobalStyle from './Global.styles';
import SnackBarProvider from 'components/@common/SnackBarProvider/SnackBarProvider';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      suspense: true,
      useErrorBoundary: true,
    },
  },
});

const App = () => {
  return (
    <QueryClientProvider client={queryClient}>
      <GlobalStyle />
      <Router>
        <Switch>
          <UserInfoProvider>
            <SnackBarProvider>
              <ModalProvider>
                <main>
                  <Route exact path={ROUTE.HOME}>
                    <AsyncBoundary rejectedFallback={<h1>임시 에러 페이지</h1>}>
                      <Home />
                    </AsyncBoundary>
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
                  <Route path="/:oauth/callback">
                    <OAuth />
                  </Route>
                </main>
              </ModalProvider>
            </SnackBarProvider>
          </UserInfoProvider>
        </Switch>
      </Router>
      <ReactQueryDevtools panelProps={{ className: 'query-dev-tools' }} initialIsOpen={false} />
    </QueryClientProvider>
  );
};

export default App;
