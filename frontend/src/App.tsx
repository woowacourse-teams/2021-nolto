import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';

import Home from 'pages/Home/Home';

const App = () => {
  return (
    <Router>
      <main>
        <Home />
      </main>
    </Router>
  );
};

export default App;
