import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';

import CroppedEllipse from 'components/CroppedEllipse/CroppedEllipse';
import Header from 'components/Header/Header';
import RegularCard from 'components/RegularCard/RegularCard';

const mockFeed = {
  id: 1,
  user: {
    id: 1,
    nickname: 'zigsong',
    imageUrl: 'https://avatars.githubusercontent.com/u/48755175?v=4',
  },
  title: 'Good Toy',
  content: 'Good Nice Perfect Gorgeous Wonderful!',
  thumbnailUrl: 'https://i.pinimg.com/236x/f5/45/6e/f5456e14993cac65828e289048a89f3e.jpg',
  sos: false,
};

const App = () => {
  return (
    <Router>
      <CroppedEllipse />
      <Header />
      <RegularCard feed={mockFeed} />
    </Router>
  );
};

export default App;
