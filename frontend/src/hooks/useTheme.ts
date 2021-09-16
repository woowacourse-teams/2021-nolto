import { useState } from 'react';

const useTheme = () => {
  const isBrowserDefaultMode = window.matchMedia && window.matchMedia('(theme: default)').matches;
  let initialTheme = isBrowserDefaultMode ? 'default' : 'thanksgiving';

  const localSettingTheme = localStorage.getItem('theme');

  if (localSettingTheme) {
    initialTheme = localSettingTheme;
  }

  const [theme, setTheme] = useState(initialTheme);

  const setMode = (mode: string) => {
    window.localStorage.setItem('theme', mode);
    setTheme(mode);
  };

  const toggleTheme = () => {
    setMode(theme === 'default' ? 'thanksgiving' : 'default');
    setTimeout(() => {
      setMode('default');
    }, 4500);
  };

  return [theme, toggleTheme] as const;
};

export default useTheme;
