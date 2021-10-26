import styled from 'styled-components';

import { MEDIA_QUERY } from 'constants/mediaQuery';

const Root = styled.div`
  display: flex;
  flex-direction: column;
  height: 100%;
`;

const RecentFeedsContainer = styled.div`
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  grid-gap: 1rem;

  @media ${MEDIA_QUERY.TABLET} {
    grid-template-columns: repeat(4, 1fr);
  }

  @media ${MEDIA_QUERY.TABLET_SMALL} {
    grid-template-columns: repeat(2, 1fr);
  }
`;

const MoreHiddenElement = styled.div`
  width: 100%;
  height: 20px;
  visibility: hidden;
`;

export default {
  Root,
  RecentFeedsContainer,
  MoreHiddenElement,
};
