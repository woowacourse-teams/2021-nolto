import styled from 'styled-components';

import { Card } from 'commonStyles';

const Root = styled(Card)`
  width: 754px;
  height: 136px;
  padding: 24px;
  display: flex;
  gap: 16px;
`;

const Thumbnail = styled.img`
  width: 88px;
  height: 88px;
  border-radius: 16px;
`;

const ContentArea = styled.div`
  margin: 8px 0;
`;

const Title = styled.h3`
  font-size: 20px;
  color: inherit;
  margin-bottom: 16px;
`;

const Content = styled.p`
  font-size: 12px;
  color: inherit;
`;

export default { Root, Thumbnail, ContentArea, Title, Content };
