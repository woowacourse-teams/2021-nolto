import React, { useEffect } from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from 'react-query';
import { ReactQueryDevtools } from 'react-query/devtools';
import { ThemeProvider } from 'styled-components';

import Page from 'pages';
import PrivateRoute from 'components/PrivateRoute';
import ErrorBoundary from 'components/ErrorBoundary';
import ErrorFallback from 'components/ErrorFallback/ErrorFallback';
import DialogProvider from 'contexts/dialog/DialogProvider';
import ModalProvider from 'contexts/modal/ModalProvider';
import SnackbarProvider from 'contexts/snackbar/SnackbarProvider';
import ROUTE from 'constants/routes';
import { ERROR_MSG } from 'constants/message';
import useTheme from 'hooks/useTheme';
import GlobalStyle from './Global.styles';
import { defaultTheme, thanksgivingTheme } from '../themes';

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
  const [themeMode, toggleThemeMode] = useTheme();
  const theme = themeMode === 'default' ? defaultTheme : thanksgivingTheme;

  return (
    <QueryClientProvider client={queryClient}>
      <ErrorBoundary fallback={<ErrorFallback message={ERROR_MSG.UNKNOWN_ERROR} />}>
        <ThemeProvider theme={theme}>
          <GlobalStyle />
          <Router>
            <Switch>
              <SnackbarProvider>
                <DialogProvider>
                  <ModalProvider>
                    <Route exact path={ROUTE.HOME}>
                      <Page.Home toggleTheme={toggleThemeMode} />
                    </Route>
                    <Route exact path={ROUTE.ABOUT}>
                      <Page.About />
                    </Route>
                    <PrivateRoute path={ROUTE.UPLOAD}>
                      <Page.Upload />
                    </PrivateRoute>
                    <PrivateRoute path={ROUTE.MODIFY}>
                      <Page.Modify />
                    </PrivateRoute>
                    <Route exact path={ROUTE.RECENT}>
                      <Page.RecentFeeds />
                    </Route>
                    <Route path={`${ROUTE.FEEDS}/:id`}>
                      <Page.FeedDetail />
                    </Route>
                    <Route path={ROUTE.SEARCH}>
                      <Page.SearchResult />
                    </Route>
                    <Route path={ROUTE.MYPAGE}>
                      <Page.Mypage />
                    </Route>
                    <Route path="/:oauth/callback">
                      <Page.OAuth />
                    </Route>
                  </ModalProvider>
                </DialogProvider>
              </SnackbarProvider>
            </Switch>
          </Router>
        </ThemeProvider>
        <ReactQueryDevtools panelProps={{ className: 'query-dev-tools' }} initialIsOpen={false} />
      </ErrorBoundary>
    </QueryClientProvider>
  );
};

export default App;
