import styled from 'styled-components';

import { MEDIA_QUERY } from 'constants/mediaQuery';

const Root = styled.div`
  display: flex;
  align-items: center;
  gap: 0.75rem;
  width: 100%;

  @media ${MEDIA_QUERY.MOBILE} {
    gap: 0.5rem;
  }
`;

const Image = styled.img`
  width: 1.5rem;
  height: 1.5rem;
  border-radius: 50%;

  @media ${MEDIA_QUERY.MOBILE} {
    width: 1.25rem;
    height: 1.25rem;
  }
`;

const Nickname = styled.span`
  font-size: 1rem;

  @media ${MEDIA_QUERY.MOBILE} {
    font-size: 0.85rem;
  }
`;

export default { Root, Image, Nickname };
