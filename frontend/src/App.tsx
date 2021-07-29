import React from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from 'react-query';
import { ReactQueryDevtools } from 'react-query/devtools';

import Home from 'pages/Home/Home';
import Upload from 'pages/Upload/Upload';
import FeedDetail from 'pages/FeedDetail/FeedDetail';
import RecentFeeds from 'pages/RecentFeeds/RecentFeeds';
import SearchResult from 'pages/SearchResult/SearchResult';
import OAuth from 'pages/OAuth/OAuth';
import Modify from 'pages/Modify/Modify';
import PrivateRoute from 'components/PrivateRoute';
import NotificationProvider from 'context/notification/NotificationProvider';
import ModalProvider from 'context/modal/ModalProvider';
import SnackBarProvider from 'context/snackBar/SnackBarProvider';
import ROUTE from 'constants/routes';
import GlobalStyle from './Global.styles';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      suspense: true,
      useErrorBoundary: true,
      retry: 1,
    },
  },
});

const App = () => {
  return (
    <QueryClientProvider client={queryClient}>
      <GlobalStyle />
      <Router>
        <Switch>
          <SnackBarProvider>
            <NotificationProvider>
              <ModalProvider>
                <main>
                  <Route exact path={ROUTE.HOME}>
                    <Home />
                  </Route>
                  <PrivateRoute path={ROUTE.UPLOAD}>
                    <Upload />
                  </PrivateRoute>
                  <PrivateRoute path={ROUTE.MODIFY}>
                    <Modify />
                  </PrivateRoute>
                  <Route exact path={ROUTE.RECENT}>
                    <RecentFeeds />
                  </Route>
                  <Route path={`${ROUTE.FEEDS}/:id`}>
                    <FeedDetail />
                  </Route>
                  <Route path={ROUTE.SEARCH}>
                    <SearchResult />
                  </Route>
                  <Route path="/:oauth/callback">
                    <OAuth />
                  </Route>
                </main>
              </ModalProvider>
            </NotificationProvider>
          </SnackBarProvider>
        </Switch>
      </Router>
      <ReactQueryDevtools panelProps={{ className: 'query-dev-tools' }} initialIsOpen={false} />
    </QueryClientProvider>
  );
};

export default App;
