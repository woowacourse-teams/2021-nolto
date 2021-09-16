import { useEffect, useState } from 'react';

const useTheme = () => {
  const [theme, setTheme] = useState('default');

  const setMode = (mode: string) => {
    localStorage.setItem('theme', mode);
    setTheme(mode);
  };

  const toggleTheme = () => {
    // TODO: setTimeoutId 삭제 리팩토링
    setMode('thanksgiving');

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