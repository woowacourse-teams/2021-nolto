import React, { SyntheticEvent } from 'react';

import { DEFAULT_IMG } from 'constants/common';

interface Props {
  thumbnailUrl: string;
  className?: string;
}

const ThumbnailImage = ({ thumbnailUrl, className }: Props) => {
  const imgExtensions = ['.apng', '.bmp', '.gif', '.jpg', '.jpeg', '.pjpeg', '.png', '.svg'];
  const thumbnailExtension = thumbnailUrl.slice(thumbnailUrl.lastIndexOf('.'));

  return imgExtensions.includes(thumbnailExtension) ? (
    <img
      className={className}
      src={thumbnailUrl}
      onError={(event: SyntheticEvent<HTMLImageElement>) => {
        event.currentTarget.src = DEFAULT_IMG.FEED;
      }}
    />
  ) : (
    <video className={className} autoPlay muted loop>
      <source src={thumbnailUrl} type="video/mp4" />
      <img
        src={thumbnailUrl}
        onError={(event: SyntheticEvent<HTMLImageElement>) => {
          event.currentTarget.src = DEFAULT_IMG.FEED;
        }}
      />
    </video>
  );
};

export default ThumbnailImage;
