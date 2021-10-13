import React, { ImgHTMLAttributes, useEffect, useRef } from 'react';

interface ImageAttributes extends ImgHTMLAttributes<HTMLImageElement> {
  fallbackSrc: string;
}

const isImageValid = (src: string) => {
  return new Promise((resolve) => {
    const img = document.createElement('img');
    img.onerror = () => resolve(false);
    img.onload = () => resolve(true);
    img.src = src;
  });
};

const Image = ({ src, alt, fallbackSrc, ...option }: ImageAttributes) => {
  const imgRef = useRef(null);

  useEffect(() => {
    isImageValid(src).then((isValid) => {
      if (!imgRef.current) return;

      if (!isValid) {
        imgRef.current.src = fallbackSrc;
      }
    });
  }, []);

  return <img {...option} ref={imgRef} src={src} alt={alt} />;
};

export default Image;
