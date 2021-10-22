import { HEIGHT } from 'constants/common';
import { BREAK_POINTS, MEDIA_QUERY } from 'constants/mediaQuery';
import styled from 'styled-components';

const Root = styled.div`
  padding-top: ${HEIGHT.HEADER};
`;

export const BaseLayoutMain = styled.main`
  max-width: ${BREAK_POINTS.DESKTOP};
  padding: 2rem 1rem;
  margin: 0 auto;
  overflow: auto;
  width: 100%;
  min-height: 100vh;

  @media ${MEDIA_QUERY.MOBILE} {
    padding: 1rem;
  }
`;

export default { Root };
