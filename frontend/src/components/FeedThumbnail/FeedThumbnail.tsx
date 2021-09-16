import React, { SyntheticEvent } from 'react';

import { DEFAULT_IMG, THUMBNAIL_EXTENSION } from 'constants/common';
import Styled from './FeedThumbnail.styles';

interface Props {
  thumbnailUrl: string;
  className?: string;
}

const ThumbnailImage = ({ thumbnailUrl, className }: Props) => {
  const imgExtensions = ['apng', 'bmp', 'gif', 'jpg', 'jpeg', 'pjpeg', 'png', 'svg'];
  const thumbnailExtension = thumbnailUrl.slice(thumbnailUrl.lastIndexOf('.')).slice(1);
  const thumbnailFileType = thumbnailUrl.slice(thumbnailUrl.lastIndexOf('=')).slice(1);

  const isThumbnailImageType =
    imgExtensions.includes(thumbnailExtension) || THUMBNAIL_EXTENSION.includes(thumbnailFileType);

  return isThumbnailImageType ? (
    <Styled.Image
      className={className}
      src={thumbnailUrl}
      onError={(event: SyntheticEvent<HTMLImageElement>) => {
        event.currentTarget.src = DEFAULT_IMG.FEED;
      }}
    />
  ) : (
    <video className={className} autoPlay muted loop>
      <source src={thumbnailUrl} type="video/mp4" />
      <Styled.Image src={DEFAULT_IMG.FEED} />
    </video>
  );
};

export default ThumbnailImage;
