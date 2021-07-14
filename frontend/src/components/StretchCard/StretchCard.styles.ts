import styled from 'styled-components';

import { Card } from 'commonStyles';
import { PALETTE } from 'constants/palette';

const Root = styled(Card)`
  position: relative;
  width: 42.375rem;
  height: 7.625rem;
  padding: 1.5rem;
  display: flex;
  gap: 1rem;
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

const Thumbnail = styled.img`
  width: 5rem;
  height: 5rem;
  border-radius: 16px;
`;

const ChipWrapper = styled.div`
  position: absolute;
  top: 16px;
  right: 16px;
`;

const ContentArea = styled.div`
  margin: 0.5rem 0;
`;

const TitleWrapper = styled.div`
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 1rem;
`;

const Title = styled.h3`
  font-size: 1.25rem;
  color: inherit;
`;

const Content = styled.p`
  font-size: 0.75rem;
  color: inherit;
`;

export default { Root, Thumbnail, ChipWrapper, ContentArea, TitleWrapper, Title, Content };
