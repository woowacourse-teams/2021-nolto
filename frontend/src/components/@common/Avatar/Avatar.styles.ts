import styled from 'styled-components';

import FeedThumbnail from 'components/FeedThumbnail/FeedThumbnail';

const Root = styled.div`
  display: flex;
  align-items: center;
  gap: 0.75rem;
  width: 100%;
`;

const Image = styled(FeedThumbnail)`
  width: 1.5rem;
  height: 1.5rem;
  border-radius: 50%;
`;

const Nickname = styled.span`
  font-size: 1rem;
`;

export default { Root, Image, Nickname };
