import React from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from 'react-query';

import Home from 'pages/Home/Home';
import Upload from 'pages/Upload/Upload';
import ROUTE from 'constants/routes';

const queryClient = new QueryClient();

const App = () => {
  return (
    <QueryClientProvider client={queryClient}>
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
    </QueryClientProvider>
  );
};

export default App;
