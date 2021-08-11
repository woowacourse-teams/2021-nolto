import styled from 'styled-components';

import { PALETTE } from 'constants/palette';
import { Card, hoverLayer } from 'commonStyles';
import { MEDIA_QUERY } from 'constants/mediaQuery';

const Root = styled(Card)<{ imageUrl: string }>`
  width: 15.75rem;
  height: 19.75rem;
  background-image: url(${({ imageUrl }) => imageUrl});
  background-size: cover;
  cursor: pointer;
  overflow: hidden;

  ${hoverLayer({})};

  @media ${MEDIA_QUERY.TABLET} {
    width: 12.5rem;
    height: 15.75rem;
  }

  @media ${MEDIA_QUERY.MOBILE} {
    width: 9.75rem;
    height: 12rem;
  }
`;

const ContentArea = styled.div`
  position: absolute;
  bottom: 0;
  width: 100%;
  height: 7rem;
  padding: 0.75rem 1rem;
  text-align: left;
  color: ${PALETTE.WHITE_400};
  background: rgba(51, 51, 51, 0.25);
  border-radius: 0px 0px 0.75rem 0.75rem;

  @media ${MEDIA_QUERY.MOBILE} {
    height: 4.5rem;
  }
`;

const Title = styled.h3`
  font-size: 1.5rem;
  color: inherit;
  margin-bottom: 8px;

  @media ${MEDIA_QUERY.MOBILE} {
    font-size: 1rem;
  }
`;

const Content = styled.p`
  font-size: 0.75rem;
  color: inherit;
  font-weight: 200;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;

  @media ${MEDIA_QUERY.MOBILE} {
    display: none;
  }
`;

export default { Root, ContentArea, Title, Content };
