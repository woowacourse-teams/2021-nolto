import React, { useEffect, useRef, useState } from 'react';
import { genNewId } from 'utils/common';
import hasWindow from 'constants/windowDetector';

interface RegisteredCallbackInfo {
  id: number;
  callback: () => void;
  targetElementRef: React.MutableRefObject<Element>;
}

const idGenerator = genNewId();

let registeredCallbackInfos: RegisteredCallbackInfo[] = [];

hasWindow &&
  document.addEventListener('mousedown', (event) => {
    registeredCallbackInfos.forEach((callbackInfo) => {
      if (!callbackInfo.targetElementRef.current) return;

      if (!callbackInfo.targetElementRef.current?.contains(event.target as Node)) {
        callbackInfo.callback();
      }
    });
  });

const useFocusOut = (callback: () => void) => {
  const [id, _] = useState(idGenerator.next().value);
  const targetElementRef = useRef(null);

  useEffect(() => {
    registeredCallbackInfos.push({
      id,
      callback: callback,
      targetElementRef,
    });

    return () => {
      registeredCallbackInfos = registeredCallbackInfos.filter(
        (callbackInfo) => callbackInfo.id !== id,
      );
    };
  }, []);

  return targetElementRef;
};

export default useFocusOut;
