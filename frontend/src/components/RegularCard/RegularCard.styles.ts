import styled from 'styled-components';

import { PALETTE } from 'constants/palette';
import { Card } from 'commonStyles';

const Root = styled(Card)<{ imageUrl: string }>`
  position: relative;
  width: 280px;
  height: 352px;
  background-image: url(${({ imageUrl }) => imageUrl});
  background-size: cover;
`;

const ContentArea = styled.div`
  position: absolute;
  bottom: 0;
  width: 100%;
  height: 108px;
  padding: 12px 16px;
  background: rgba(51, 51, 51, 0.25);
  backdrop-filter: blur(2px);
  border-radius: 0px 0px 20px 20px;
  color: ${PALETTE.WHITE_400};
`;

const Title = styled.h3`
  font-size: 26px;
  color: inherit;
  margin-bottom: 8px;
`;

const Content = styled.p`
  font-size: 12px;
  color: inherit;
  font-weight: 200;
`;

export default { Root, ContentArea, Title, Content };
