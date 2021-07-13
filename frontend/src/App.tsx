import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';

import BaseLayout from 'components/BaseLayout/BaseLayout';

const App = () => {
  return (
    <Router>
      <BaseLayout>놀토 페이지</BaseLayout>
    </Router>
  );
};

export default App;
