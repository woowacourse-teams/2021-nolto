import styled from 'styled-components';

import { Card, hoverLayer } from 'commonStyles';
import { MEDIA_QUERY } from 'constants/mediaQuery';

const Root = styled(Card)`
  width: 40rem;
  height: 8rem;
  padding: 0.5rem 1.25rem;
  display: flex;
  align-items: center;
  gap: 1rem;
  cursor: pointer;
  overflow: hidden;

  ${hoverLayer({})};

  @media ${MEDIA_QUERY.TABLET} {
    width: 32rem;
    height: 6.5rem;
    padding: 0.35rem 1rem;
  }

  @media ${MEDIA_QUERY.MOBILE} {
    width: 18rem;
    height: 5rem;
    padding: 0.2rem 0.65rem;
  }
`;

const Thumbnail = styled.img`
  width: 5.5rem;
  height: 5.5rem;
  flex-shrink: 0;
  border-radius: 0.5rem;

  @media ${MEDIA_QUERY.TABLET} {
    width: 4.5rem;
    height: 4.5rem;
  }

  @media ${MEDIA_QUERY.TABLET} {
    width: 3.5rem;
    height: 3.5rem;
  }
`;

const ChipWrapper = styled.div`
  position: absolute;
  top: 0.75rem;
  right: 0.75rem;

  @media ${MEDIA_QUERY.MOBILE} {
    display: none;
  }
`;

const ContentArea = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
`;

const TitleWrapper = styled.div`
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1rem;
`;

const Title = styled.h3`
  font-size: 1rem;
  color: inherit;
`;

const Content = styled.div`
  font-size: 0.75rem;
  color: inherit;
`;

export default { Root, Thumbnail, ChipWrapper, ContentArea, TitleWrapper, Title, Content };
