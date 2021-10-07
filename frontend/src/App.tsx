import React from 'react';
import { Switch, Route } from 'react-router-dom';
import { ReactQueryDevtools } from 'react-query/devtools';
import { ThemeProvider } from 'styled-components';

import Page from 'pages';
import PrivateRoute from 'components/PrivateRoute';
import ErrorBoundary from 'components/ErrorBoundary';
import ErrorFallback from 'components/ErrorFallback/ErrorFallback';
import DialogProvider from 'contexts/dialog/DialogProvider';
import ModalProvider from 'contexts/modal/ModalProvider';
import SnackbarProvider from 'contexts/snackbar/SnackbarProvider';
import MemberProvider from 'contexts/member/MemberProvider';
import ROUTE from 'constants/routes';
import { ERROR_MSG } from 'constants/message';
import useTheme from 'hooks/useTheme';
import GlobalStyle from './Global.styles';
import { defaultTheme, thanksgivingTheme } from '../themes';

const App = () => {
  const [themeMode, toggleThemeMode] = useTheme();
  const theme = themeMode === 'default' ? defaultTheme : thanksgivingTheme;

  return (
    <ErrorBoundary fallback={<ErrorFallback message={ERROR_MSG.UNKNOWN_ERROR} />}>
      <ThemeProvider theme={theme}>
        <GlobalStyle />
        <Switch>
          <SnackbarProvider>
            <DialogProvider>
              <ModalProvider>
                <MemberProvider>
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
                </MemberProvider>
              </ModalProvider>
            </DialogProvider>
          </SnackbarProvider>
        </Switch>
      </ThemeProvider>
      <ReactQueryDevtools panelProps={{ className: 'query-dev-tools' }} initialIsOpen={false} />
    </ErrorBoundary>
  );
};

export default App;
