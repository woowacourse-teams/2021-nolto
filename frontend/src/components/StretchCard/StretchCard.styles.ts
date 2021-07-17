import styled from 'styled-components';

import { Card } from 'commonStyles';
import { PALETTE } from 'constants/palette';

const Root = styled(Card)`
  position: relative;
  width: 34rem;
  height: 6rem;
  padding: 0.75rem 1rem;
  display: flex;
  align-items: center;
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
  width: 4.25rem;
  height: 4.25rem;
  border-radius: 0.5rem;
`;

const ChipWrapper = styled.div`
  position: absolute;
  top: 0.75rem;
  right: 0.75rem;
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

const Content = styled.p`
  font-size: 0.75rem;
  color: inherit;
`;

export default { Root, Thumbnail, ChipWrapper, TitleWrapper, Title, Content };
