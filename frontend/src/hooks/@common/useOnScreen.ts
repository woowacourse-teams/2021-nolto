import { useEffect, useState } from 'react';

import hasWindow from 'constants/windowDetector';

const useOnScreen = (ref: React.RefObject<HTMLElement>) => {
  const [isIntersecting, setIntersecting] = useState(false);

  const observer =
    hasWindow && new IntersectionObserver(([entry]) => setIntersecting(entry.isIntersecting));

  useEffect(() => {
    observer?.observe(ref.current);
    return () => {
      observer?.disconnect();
    };
  }, []);

  return isIntersecting;
};

export default useOnScreen;
