import { useState, useEffect } from 'react';

const useQueryDebounce = (value: string, delay = 200) => {
  const [debounceValue, setDebounceValue] = useState(value);

  useEffect(() => {
    const handler: NodeJS.Timeout = setTimeout(() => {
      setDebounceValue(value);
    }, delay);

    return () => {
      clearTimeout(handler);
    };
  }, [value, delay]);

  return debounceValue;
};

export default useQueryDebounce;
