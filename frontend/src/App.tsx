import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';

import CroppedEllipse from 'components/CroppedEllipse/CroppedEllipse';
import Header from 'components/Header/Header';

const App = () => {
  return (
    <Router>
      <CroppedEllipse />
      <Header />
    </Router>
  );
};

export default App;
