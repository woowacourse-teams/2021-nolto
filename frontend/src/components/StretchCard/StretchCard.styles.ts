import styled from 'styled-components';

import { Card } from 'commonStyles';

const Root = styled(Card)`
  position: relative;
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

const ChipWrapper = styled.div`
  position: absolute;
  top: 16px;
  right: 16px;
`;

const ContentArea = styled.div`
  margin: 8px 0;
`;

const TitleWrapper = styled.div`
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 16px;
`;

const Title = styled.h3`
  font-size: 20px;
  color: inherit;
`;

const Content = styled.p`
  font-size: 12px;
  color: inherit;
`;

export default { Root, Thumbnail, ChipWrapper, ContentArea, TitleWrapper, Title, Content };
