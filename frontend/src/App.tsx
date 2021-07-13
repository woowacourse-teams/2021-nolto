import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';

import BaseLayout from 'components/BaseLayout/BaseLayout';
import Home from 'pages/Home/Home';

const App = () => {
  return (
    <Router>
      <BaseLayout>
        <Home />
      </BaseLayout>
    </Router>
  );
};

export default App;
