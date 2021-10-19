import React from 'react';

import { DEFAULT_IMG } from 'constants/common';
import Styled from './Thumbnail.styles';

interface Props {
  thumbnailUrl: string;
  alt?: string;
  className?: string;
}

const VIDEO_EXTENSIONS = ['mp4'];

const isVideoType = (url: string) => {
  const thumbnailExtension = url.match(/[^.]+$/)[0];

  return VIDEO_EXTENSIONS.includes(thumbnailExtension);
};

const Thumbnail = ({ thumbnailUrl, alt, className }: Props) => {
  return isVideoType(thumbnailUrl) ? (
    <video className={className} autoPlay muted loop>
      <source src={thumbnailUrl} type="video/mp4" />
      <Styled.Image src={DEFAULT_IMG.FEED} alt="기본 이미지" fallbackSrc={DEFAULT_IMG.FEED} />
    </video>
  ) : (
    <Styled.Image
      className={className}
      src={thumbnailUrl}
      alt={alt}
      fallbackSrc={DEFAULT_IMG.FEED}
    />
  );
};

export default Thumbnail;
