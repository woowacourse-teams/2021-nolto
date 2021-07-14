import styled from 'styled-components';

import { PALETTE } from 'constants/palette';
import { Card } from 'commonStyles';

const Root = styled(Card)<{ imageUrl: string }>`
  position: relative;
  width: 15.75rem;
  height: 19.75rem;
  background-image: url(${({ imageUrl }) => imageUrl});
  background-size: cover;
  cursor: pointer;
  overflow: hidden;

  &:hover::after {
    content: '';
    display: block;
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    opacity: 0.3;
    background-color: ${PALETTE.BLACK_400};
  }
`;

const ContentArea = styled.div`
  position: absolute;
  bottom: 0;
  width: 100%;
  height: 6.75rem;
  padding: 0.75rem 1rem;
  background: rgba(51, 51, 51, 0.25);
  backdrop-filter: blur(2px);
  border-radius: 0px 0px 20px 20px;
  color: ${PALETTE.WHITE_400};
`;

const Title = styled.h3`
  font-size: 1.625rem;
  color: inherit;
  margin-bottom: 8px;
`;

const Content = styled.p`
  font-size: 0.75rem;
  color: inherit;
  font-weight: 200;
`;

export default { Root, ContentArea, Title, Content };
