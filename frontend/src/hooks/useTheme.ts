import { useEffect, useState } from 'react';

import hasWindow from 'constants/windowDetector';

const useTheme = () => {
  const [theme, setTheme] = useState('default');

  const setMode = (mode: string) => {
    if (hasWindow) {
      localStorage.setItem('theme', mode);
    }

    setTheme(mode);
  };

  const toggleTheme = () => {
    // TODO: setTimeoutId 삭제 리팩토링
    setMode('halloween');

    setTimeout(() => {
      setMode('default');
    }, 4500);
  };

  useEffect(() => {
    setMode('default');
  }, []);

  return [theme, toggleTheme] as const;
};

export default useTheme;
