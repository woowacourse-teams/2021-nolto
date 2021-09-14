import { useEffect, useRef } from 'react';

const useIntersectionObserver = (callback: () => void) => {
  const targetElement = useRef(null);
  const freshCallback = useRef(callback);

  useEffect(() => {
    freshCallback.current = callback;
  }, [callback]);

  useEffect(() => {
    if (!targetElement || !targetElement.current) return;

    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            freshCallback.current();
          }
        });
      },
      { threshold: 0.5 },
    );

    observer.observe(targetElement.current);

    return () => {
      observer.disconnect();
    };
  }, []);

  return targetElement;
};

export default useIntersectionObserver;
