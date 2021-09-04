import styled from 'styled-components';

import { PALETTE } from 'constants/palette';
import { Card, hoverLayer } from 'commonStyles';
import { FONT_SIZE } from 'constants/styles';

const Root = styled.div`
  position: relative;
  width: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  gap: 0.5rem;
`;

const FeedContainer = styled(Card)`
  padding-top: 125%;
  overflow: hidden;
  position: relative;

  & > img {
    position: absolute;
    top: 0;
    left: 0;
    height: 100%;
    width: 100%;
  }

  ${hoverLayer({})}
`;

const ContentWrapper = styled.div`
  position: absolute;
  bottom: 0;
  width: 100%;
  height: 7rem;
  padding: 0.75rem 1rem;
  text-align: left;
  color: ${PALETTE.WHITE_400};
  background: rgba(51, 51, 51, 0.25);
  border-radius: 0px 0px 0.75rem 0.75rem;
`;

const Title = styled.h3`
  font-size: 1.5rem;
  color: inherit;
  margin-bottom: 8px;
`;

const Content = styled.p`
  font-size: ${FONT_SIZE.SMALL};
  color: inherit;
  font-weight: 200;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
`;

export default { Root, FeedContainer, ContentWrapper, Title, Content };
