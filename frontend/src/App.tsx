import React from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from 'react-query';
import { ReactQueryDevtools } from 'react-query/devtools';

import Home from 'pages/Home/Home';
import Upload from 'pages/Upload/Upload';
import ROUTE from 'constants/routes';
import GlobalStyle from './Global.styles';

const queryClient = new QueryClient();

const App = () => {
  return (
    <QueryClientProvider client={queryClient}>
      <GlobalStyle />
      <Router>
        <Switch>
          <>
            <main>
              <Route exact path={ROUTE.HOME}>
                <Home />
              </Route>
              <Route path={ROUTE.UPLOAD}>
                <Upload />
              </Route>
            </main>
          </>
        </Switch>
      </Router>
      <ReactQueryDevtools panelProps={{ className: 'query-dev-tools' }} initialIsOpen={false} />
    </QueryClientProvider>
  );
};

export default App;
