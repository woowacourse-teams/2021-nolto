import { HEIGHT } from 'constants/common';
import { BREAK_POINTS, MEDIA_QUERY } from 'constants/mediaQuery';
import styled from 'styled-components';

const Root = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: ${HEIGHT.HEADER};
`;

export const BaseLayoutMain = styled.main`
  max-width: ${BREAK_POINTS.DESKTOP};
  padding: 2rem 1rem 0;
  margin-right: auto;
  margin-left: auto;

  @media ${MEDIA_QUERY.DESKTOP} {
    width: 100%;
  }
`;

export default { Root };
